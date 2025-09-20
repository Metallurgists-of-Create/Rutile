package dev.metallurgists.rutile.api.material.registry.fluid;

import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.flag.FlagKey;
import dev.metallurgists.rutile.api.material.flag.types.IFluidRegistry;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class FluidFlagProperties {
    @Getter
    final Material material;
    @Getter
    final FlagKey<? extends IFluidRegistry> flagKey;

    /**
     * Example: "%s/%s"<p>%s(1) -> Material path</p>%s(2) -> Flag Key name (_flow appended to end)
     */
    @Getter @Setter
    String textureFormat = "%s/%s";
    /**
     * density in g/cm^3
     */
    @Getter @Setter
    double density = 1;
    /**
     * viscosity of the fluid in Poise
     */
    @Getter @Setter
    double viscosity = 0.01;
    /**
     * luminosity in Light Levels<p>min -> 0</p>max -> 15
     */
    @Getter @Setter
    int luminosity = 0;
    /**
     * colour in RGB.<p>If left as-is, will use Material colour</p>
     */
    @Getter @Setter
    int color = 0xFFFFFF;
    /**
     * By default, the fluid temperature is the melting point of the Material.
     */
    @Getter @Setter
    double temperature = material.materialInfo().meltingPoint() * 12.73913043;
    /**
     * Ticks the bucket will burn for when used as furnace fuel.
     */
    @Getter @Setter
    int burnTime = 0;
}
