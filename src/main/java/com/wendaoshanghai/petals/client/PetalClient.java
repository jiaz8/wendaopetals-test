package com.wendaoshanghai.petals.client;

import com.wendaoshanghai.petals.WendaoPetals;
import com.wendaoshanghai.petals.config.PetalConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** Client-only emission: never changes blocks, chunks, biomes or the server world. */
@Mod.EventBusSubscriber(modid = WendaoPetals.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public final class PetalClient {
    private PetalClient() {}

    @SubscribeEvent public static void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !PetalConfig.ENABLED.get()) return;
        Minecraft mc = Minecraft.getInstance();
        ClientLevel level = mc.level;
        if (level == null || mc.player == null || mc.isPaused()) return;
        RandomSource random = level.random;
        int range = PetalConfig.SPAWN_RADIUS.get();
        int height = PetalConfig.VERTICAL_RADIUS.get();
        int attempts = PetalConfig.SAMPLE_ATTEMPTS.get();
        int limit = PetalConfig.MAX_SPAWNS_PER_TICK.get();
        if (attempts == 0 || limit == 0) return;
        BlockPos player = mc.player.blockPosition();
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        int count = 0;

        for (int i = 0; i < attempts && count < limit; i++) {
            int x = player.getX() + random.nextInt(2 * range + 1) - range;
            int y = player.getY() + random.nextInt(2 * height + 1) - height;
            int z = player.getZ() + random.nextInt(2 * range + 1) - range;
            pos.set(x, y, z);
            if (!level.hasChunkAt(pos)) continue;
            BlockState state = level.getBlockState(pos);
            PetalColor kind = FlowerRule.get(level, pos, state);
            if (kind == null || random.nextDouble() >= PetalConfig.SPAWN_CHANCE.get()) continue;
            pos.move(0, -1, 0);
            // Emit only at an exposed underside to prevent particles starting inside dense canopies.
            if (!level.getBlockState(pos).isAir()) continue;
            int depth = distanceToFloor(level, pos);
            if (depth < 2) continue;
            double px = x + random.nextDouble();
            double py = y - 0.05D;
            double pz = z + random.nextDouble();
            // ParticleProvider interprets dy as estimated downward flight distance.
            level.addParticle(kind.type(), px, py, pz, 0D, depth, 0D);
            count++;
            pos.move(0, 1, 0);
        }
    }

    private static int distanceToFloor(ClientLevel level, BlockPos.MutableBlockPos pos) {
        int limit = Math.min(52, PetalConfig.MAX_LIFETIME.get() / 26);
        BlockPos.MutableBlockPos scan = pos.mutable();
        for (int n = 0; n < limit && scan.getY() > level.getMinBuildHeight() + 1; n++) {
            scan.move(0, -1, 0);
            if (!level.hasChunkAt(scan)) return 0;
            if (!level.getBlockState(scan).isAir()) return n + 1;
        }
        return Math.max(4, limit);
    }
}
