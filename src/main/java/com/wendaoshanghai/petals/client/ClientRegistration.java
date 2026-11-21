package com.wendaoshanghai.petals.client;

import com.wendaoshanghai.petals.WendaoPetals;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = WendaoPetals.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientRegistration {
    private ClientRegistration() {}
    @SubscribeEvent public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(WendaoPetals.PINK.get(), s -> new PetalParticle.Provider(s, PetalColor.PINK));
        event.registerSpriteSet(WendaoPetals.CRIMSON.get(), s -> new PetalParticle.Provider(s, PetalColor.CRIMSON));
        event.registerSpriteSet(WendaoPetals.PALE.get(), s -> new PetalParticle.Provider(s, PetalColor.PALE));
        event.registerSpriteSet(WendaoPetals.GOLD.get(), s -> new PetalParticle.Provider(s, PetalColor.GOLD));
        event.registerSpriteSet(WendaoPetals.PURPLE.get(), s -> new PetalParticle.Provider(s, PetalColor.PURPLE));
    }
}
