package com.wendaoshanghai.petals.client;

import com.wendaoshanghai.petals.config.PetalConfig;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.CherryParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

/** Inherit Minecraft's actual 1.20.1 cherry spin/curve code, rather than imitating it. */
final class PetalParticle extends CherryParticle {
    private final double driftPhase;

    private PetalParticle(ClientLevel level, double x, double y, double z, SpriteSet sprites, PetalColor color, int flightTicks) {
        super(level, x, y, z, sprites);
        this.driftPhase = this.random.nextDouble() * Math.PI * 2;
        setColor(color.r, color.g, color.b);
        setLifetime(Mth.clamp(flightTicks, 100, PetalConfig.MAX_LIFETIME.get()));
        this.hasPhysics = true;
        this.yd = -0.035D - this.random.nextDouble() * 0.018D;
    }

    @Override public void tick() {
        if (this.onGround) { this.remove(); return; }
        super.tick(); // Vanilla cherry-leaf wobble and rotation
        if (this.onGround) { this.remove(); return; }
        // Keep sufficient downward travel, even in windy/high canopies.
        this.yd = Math.min(this.yd, -0.034D);
        double[] wind = LivingWindCompat.sample(this.x, this.y, this.z, this.level.getGameTime());
        double drift = PetalConfig.WIND_FORCE.get();
        this.xd = Mth.clamp(this.xd + wind[0] * drift + Math.sin(this.age * 0.06 + driftPhase) * 0.0007, -0.18D, 0.18D);
        this.zd = Mth.clamp(this.zd + wind[1] * drift + Math.cos(this.age * 0.06 + driftPhase) * 0.0007, -0.18D, 0.18D);
    }

    static final class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;
        private final PetalColor color;
        Provider(SpriteSet sprites, PetalColor color) { this.sprites = sprites; this.color = color; }

        @Override public Particle createParticle(SimpleParticleType type, ClientLevel level,
                double x, double y, double z, double dx, double dy, double dz) {
            // dy encodes source-canopy-to-ground height, not velocity, by agreement with PetalClient.
            int lifetime = 135 + (int)(Math.max(0, dy) * 27.0D);
            return new PetalParticle(level, x, y, z, sprites, color, lifetime);
        }
    }
}
