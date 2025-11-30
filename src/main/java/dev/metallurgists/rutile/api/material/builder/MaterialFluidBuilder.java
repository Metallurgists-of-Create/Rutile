package dev.metallurgists.rutile.api.material.builder;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.material.Fluid;

public abstract class MaterialFluidBuilder extends MaterialRegistryBuilder<Fluid> {

    protected MaterialFluidBuilder(String nameFormat) {
        super(Registries.FLUID, nameFormat);
    }
}
