package com.wendaoshanghai.petals.client;

import java.lang.reflect.Method;

/** No hard dependency on Living Wind. Resolve its public 1.4.0 client API only if present. */
final class LivingWindCompat {
    private LivingWindCompat() {}
    private static final Method WIND_X;
    private static final Method WIND_Z;
    static {
        Method mx = null, mz = null;
        try {
            Class<?> api = Class.forName("com.macatmovie.livingwind.api.ClientWindApi");
            mx = api.getMethod("windXAt", double.class, double.class, double.class);
            mz = api.getMethod("windZAt", double.class, double.class, double.class);
        } catch (ReflectiveOperationException | LinkageError ignored) { }
        WIND_X = mx;
        WIND_Z = mz;
    }
    static double[] sample(double x, double y, double z, long gameTime) {
        if (WIND_X != null && WIND_Z != null) {
            try {
                Object dx = WIND_X.invoke(null, x, y, z);
                Object dz = WIND_Z.invoke(null, x, y, z);
                return new double[] { ((Number) dx).doubleValue(), ((Number) dz).doubleValue() };
            } catch (ReflectiveOperationException | LinkageError ignored) { }
        }
        // Gentle deterministic breeze if Living Wind is missing or has no wind state.
        return new double[] { Math.sin(gameTime * 0.015) * 0.8, Math.cos(gameTime * 0.013) * 0.8 };
    }
}
