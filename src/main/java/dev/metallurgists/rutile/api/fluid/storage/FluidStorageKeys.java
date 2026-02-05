package dev.metallurgists.rutile.api.fluid.storage;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.fluid.FluidState;
import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.material.flags.FlagKey;
import dev.metallurgists.rutile.api.registry.flags.registry.FluidFlag;
import dev.metallurgists.rutile.datagen.RutileTags;
import org.jetbrains.annotations.NotNull;

public class FluidStorageKeys {

    public static final FluidStorageKey LIQUID = new FluidStorageKey(Rutile.id("liquid"),
            m -> prefixedRegisteredName("liquid_", FluidStorageKeys.LIQUID, m),
            m -> "rutile.fluid.generic",
            FluidState.LIQUID, 0);

    public static final FluidStorageKey GAS = new FluidStorageKey(Rutile.id("gas"),
            m -> suffixedRegisteredName("_gas", FluidStorageKeys.GAS, m),
            m -> {
                if (m.isElement()) {
                    FluidFlag flag = m.getFlag(FlagKey.FLUID);
                    if (m.isElement() || (flag != null && flag.getPrimaryKey() != FluidStorageKeys.LIQUID)) {
                        return "rutile.fluid.gas_generic";
                    }
                }
                return "rutile.fluid.generic";
            },
            FluidState.GAS, 0);

    public static final FluidStorageKey PLASMA = new FluidStorageKey(Rutile.id("plasma"),
            m -> m.getName() + "_plasma",
            m -> "rutile.fluid.plasma",
            FluidState.PLASMA, -1);

    public static final FluidStorageKey MOLTEN = new FluidStorageKey(Rutile.id("molten"), RutileTags.MOLTEN_FLUIDS,
            m -> "molten_" + m.getName(),
            m -> "rutile.fluid.molten",
            FluidState.LIQUID, -1);

    private FluidStorageKeys() {}

    private static @NotNull String prefixedRegisteredName(@NotNull String prefix, @NotNull FluidStorageKey key, @NotNull Material material) {
        FluidFlag flag = material.getFlag(FlagKey.FLUID);
        if (flag != null && flag.getPrimaryKey() != key) {
            return prefix + material.getName();
        }
        return material.getName();
    }

    private static @NotNull String suffixedRegisteredName(@NotNull String suffix, @NotNull FluidStorageKey key, @NotNull Material material) {
        FluidFlag flag = material.getFlag(FlagKey.FLUID);
        if (flag != null && flag.getPrimaryKey() != key) {
            return material.getName() + suffix;
        }
        return material.getName();
    }
}
