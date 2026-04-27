package dev.metallurgists.rutile.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.material.module.variable.VariableKey;
import net.minecraft.util.ExtraCodecs;

public class RutileVariableKeys {

    /**
     * Makes items under a Material usable as furnace fuel.
     */
    public static final VariableKey<Integer> BURN_TIME = create("burn_time", Integer.class, 0);
    /**
     * Used to determine the colour of tinted objects.
     */
    public static final VariableKey<Integer> COLOUR = create("colour", Integer.class, 0xFFFFFF);
    /**
     * Used to determine the unit amount of a Material
     */
    public static final VariableKey<Long> MATERIAL_AMOUNT = create("material_amount", Long.class, -1L);
    /**
     * Used to determine the harvest tier of a Material
     */
    public static final VariableKey<Integer> HARVEST_TIER = create("harvest_tier", Integer.class, 2);


    private static <T> VariableKey<T> create(String name, Class<T> clazz, T defaultValue) {
        return new VariableKey<>(Rutile.id(name), clazz, defaultValue);
    }

    public static void init() {}
}
