package com.wendaoshanghai.petals;

import com.wendaoshanghai.petals.config.PetalConfig;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod(WendaoPetals.MOD_ID)
public final class WendaoPetals {
    public static final String MOD_ID = "wendaopetals";
    public static final DeferredRegister<net.minecraft.core.particles.ParticleType<?>> PARTICLES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, MOD_ID);

    public static final RegistryObject<SimpleParticleType> PINK = particle("pink");
    public static final RegistryObject<SimpleParticleType> CRIMSON = particle("crimson");
    public static final RegistryObject<SimpleParticleType> PALE = particle("pale");
    public static final RegistryObject<SimpleParticleType> GOLD = particle("gold");
    public static final RegistryObject<SimpleParticleType> PURPLE = particle("purple");

    private static RegistryObject<SimpleParticleType> particle(String id) {
        return PARTICLES.register(id, () -> new SimpleParticleType(false));
    }

    public WendaoPetals() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        PARTICLES.register(bus);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, PetalConfig.SPEC);
    }
}
