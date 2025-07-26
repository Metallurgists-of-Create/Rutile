package dev.metallurgists.rutile.api.material.registry.fluid;

import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.flag.types.IFluidRegistry;

public interface IMaterialFluid {
    public Material getMaterial();

    public IFluidRegistry getFlag();
}
