package com.wendaoshanghai.petals.client;

import com.wendaoshanghai.petals.config.PetalConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import java.util.List;

final class FlowerRule {
    private FlowerRule() {}

    static PetalColor get(Level level, BlockPos pos, BlockState state) {
        ResourceLocation key = ForgeRegistries.BLOCKS.getKey(state.getBlock());
        if (key == null) return null;
        String id = key.toString();
        List<? extends String> rules = PetalConfig.FLOWER_LEAVES.get();
        for (String rule : rules) {
            int idx = rule.indexOf('=');
            if (idx < 1 || !id.equals(rule.substring(0, idx))) continue;
            PetalColor color = PetalColor.fromName(rule.substring(idx + 1));
            if (id.equals("xiaoxiang_cultivation:peach_leaves") && nearBloodPeachLog(level, pos)) {
                return PetalColor.CRIMSON;
            }
            return color;
        }
        return null;
    }

    // Ordinary peach and blood peach share a leaves ID in the uploaded xiaoxiang 0.3.377.
    // A nearby trunk provides a best-effort disambiguation. This is not guaranteed for distant branches.
    private static boolean nearBloodPeachLog(Level level, BlockPos pos) {
        BlockPos.MutableBlockPos p = new BlockPos.MutableBlockPos();
        for (int dx = -3; dx <= 3; dx++) {
            for (int dz = -3; dz <= 3; dz++) {
                for (int dy = -5; dy <= 2; dy++) {
                    p.set(pos.getX() + dx, pos.getY() + dy, pos.getZ() + dz);
                    if (!level.hasChunkAt(p)) continue;
                    ResourceLocation id = ForgeRegistries.BLOCKS.getKey(level.getBlockState(p).getBlock());
                    if (id != null && id.toString().equals("xiaoxiang_cultivation:blood_peach_log")) return true;
                }
            }
        }
        return false;
    }
}
