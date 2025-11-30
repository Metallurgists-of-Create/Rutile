package dev.metallurgists.rutile.api.material.builder;

import dev.metallurgists.rutile.api.material.FlagRegistryTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.material.Fluid;

public class FluidFlagContainer extends FlagContainer<Fluid> {

    public FluidFlagContainer() {
        super(FlagRegistryTypes.FLUID);
    }

    @Override
    public Registry<Fluid> getRegistry() {
        return BuiltInRegistries.FLUID;
    }

    @Override
    public Fluid get(ResourceKey<Fluid> registryKey) {
        return getRegistry().get(registryKey);
    }
}
