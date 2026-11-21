package com.wendaoshanghai.petals.config;

import net.minecraftforge.common.ForgeConfigSpec;
import java.util.List;

public final class PetalConfig {
    private PetalConfig() {}
    private static final ForgeConfigSpec.Builder B = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.BooleanValue ENABLED;
    public static final ForgeConfigSpec.BooleanValue SUPPRESS_RUSTLING_CHERRY;
    public static final ForgeConfigSpec.IntValue SAMPLE_ATTEMPTS;
    public static final ForgeConfigSpec.IntValue SPAWN_RADIUS;
    public static final ForgeConfigSpec.IntValue VERTICAL_RADIUS;
    public static final ForgeConfigSpec.IntValue MAX_SPAWNS_PER_TICK;
    public static final ForgeConfigSpec.DoubleValue SPAWN_CHANCE;
    public static final ForgeConfigSpec.DoubleValue WIND_FORCE;
    public static final ForgeConfigSpec.IntValue MAX_LIFETIME;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> FLOWER_LEAVES;

    static {
        B.push("petals");
        ENABLED = B.comment("Master switch (client only)").define("enabled", true);
        SUPPRESS_RUSTLING_CHERRY = B.comment("Suppress Rustling Leaves replacement only for vanilla cherry animateTick; other trees remain unaffected")
                .define("suppressRustlingCherry", true);
        SAMPLE_ATTEMPTS = B.comment("Random nearby blocks examined per client tick. Reduce for weak PCs")
                .defineInRange("sampleAttempts", 140, 0, 600);
        SPAWN_RADIUS = B.defineInRange("horizontalRange", 22, 4, 64);
        VERTICAL_RADIUS = B.defineInRange("verticalRange", 18, 4, 48);
        MAX_SPAWNS_PER_TICK = B.defineInRange("maxSpawnPerTick", 9, 0, 40);
        SPAWN_CHANCE = B.defineInRange("spawnChancePerLeaf", 0.75D, 0.0D, 1.0D);
        WIND_FORCE = B.defineInRange("windForce", 0.0035D, 0.0D, 0.1D);
        MAX_LIFETIME = B.comment("Maximum airborne lifetime in game ticks (20 ticks = 1 second)")
                .defineInRange("maxLifeTicks", 800, 20, 2400);
        FLOWER_LEAVES = B.comment("BlockID=color. Supported colors: pink, crimson, pale, gold, purple. Use registered LEAF block IDs, not biome names")
                .defineList("flowerLeaves", List.of(
                    "minecraft:cherry_leaves=pink",
                    "xiaoxiang_cultivation:peach_leaves=pink"
                ), v -> v instanceof String s && s.matches("[a-z0-9_.-]+:[a-z0-9_./-]+=(pink|crimson|pale|gold|purple)"));
        B.pop();
        SPEC = B.build();
    }
}
