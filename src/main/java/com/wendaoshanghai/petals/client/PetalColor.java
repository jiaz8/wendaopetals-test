package com.wendaoshanghai.petals.client;

import com.wendaoshanghai.petals.WendaoPetals;
import net.minecraft.core.particles.SimpleParticleType;

public enum PetalColor {
    PINK(1.00f, 1.00f, 1.00f),
    CRIMSON(0.90f, 0.42f, 0.57f),
    PALE(1.00f, 0.98f, 0.95f),
    GOLD(1.00f, 0.93f, 0.50f),
    PURPLE(0.72f, 0.54f, 0.98f);

    public final float r, g, b;
    PetalColor(float r, float g, float b) { this.r = r; this.g = g; this.b = b; }
    public static PetalColor fromName(String name) {
        try { return valueOf(name.toUpperCase(java.util.Locale.ROOT)); }
        catch (IllegalArgumentException e) { return PINK; }
    }
    public SimpleParticleType type() {
        return switch (this) {
            case PINK -> WendaoPetals.PINK.get();
            case CRIMSON -> WendaoPetals.CRIMSON.get();
            case PALE -> WendaoPetals.PALE.get();
            case GOLD -> WendaoPetals.GOLD.get();
            case PURPLE -> WendaoPetals.PURPLE.get();
        };
    }
}
