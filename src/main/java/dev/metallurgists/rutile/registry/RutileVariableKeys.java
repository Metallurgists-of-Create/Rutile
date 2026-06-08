package dev.metallurgists.rutile.registry;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.material.variable.VariableKey;

public class RutileVariableKeys {

    public static final VariableKey<Integer> BURN_TIME = create("burn_time", Integer.class, 0);
    public static final VariableKey<Integer> COLOUR = create("colour", Integer.class, 0xFFFFFF);
    public static final VariableKey<Integer> HARVEST_TIER = create("harvest_tier", Integer.class, 2);

    private static <T> VariableKey<T> create(String name, Class<T> clazz, T defaultValue) {
        return new VariableKey<>(Rutile.getResource(name), clazz, defaultValue);
    }

    public static void init() {}
}
