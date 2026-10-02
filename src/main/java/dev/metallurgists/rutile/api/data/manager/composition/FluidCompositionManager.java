package dev.metallurgists.rutile.api.data.manager.composition;

import dev.metallurgists.rutile.Rutile;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.material.Fluid;

public class FluidCompositionManager extends AbstractCompositionManager<Fluid> {
    public static FluidCompositionManager INSTANCE = new FluidCompositionManager();

    public FluidCompositionManager() {
        super(Rutile.id("fluid"), Registries.FLUID, BuiltInRegistries.FLUID);
    }

    public static FluidCompositionManager getInstance() {
        return INSTANCE;
    }
}