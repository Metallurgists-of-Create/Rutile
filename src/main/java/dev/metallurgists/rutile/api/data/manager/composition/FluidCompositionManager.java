package dev.metallurgists.rutile.api.data.manager.composition;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.composition.Composition;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.material.Fluid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FluidCompositionManager extends AbstractCompositionManager<Fluid> {
    public static FluidCompositionManager INSTANCE = new FluidCompositionManager();

    public Map<Fluid, Composition<Fluid>> compositions = new HashMap<>();
    public List<Fluid> composed = new ArrayList<>();

    public FluidCompositionManager() {
        super(Rutile.id("fluid"), Registries.FLUID, Composition.codec(Registries.FLUID, BuiltInRegistries.FLUID.byNameCodec()));
    }

    public static FluidCompositionManager getInstance() {
        return INSTANCE;
    }

    @Override
    public Registry<Fluid> getRegistry() {
        return BuiltInRegistries.FLUID;
    }

    @Override
    public Map<Fluid, Composition<Fluid>> getCompositions() {
        return this.compositions;
    }

    @Override
    public List<Fluid> getComposed() {
        return this.composed;
    }

    @Override
    public void clearData() {
        this.compositions.clear();
        this.composed.clear();
    }

    @Override
    public void putComposition(Fluid composed, Composition<Fluid> composition) {
        this.compositions.put(composed, composition);
        this.composed.add(composed);
    }
}