package dev.metallurgists.rutile.api.data.server.manager.composition;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.RutileApi;
import dev.metallurgists.rutile.api.composition.Composition;
import dev.metallurgists.rutile.api.composition.RutileCompositions;
import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.registry.RutileRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MaterialCompositionManager extends AbstractCompositionManager<Material> {
    public static MaterialCompositionManager INSTANCE = new MaterialCompositionManager();

    public Map<Material, Composition> compositions = new HashMap<>();
    public List<Material> composed = new ArrayList<>();

    public MaterialCompositionManager() {
        super(Rutile.id("material"), RutileRegistries.MATERIAL_REGISTRY);
    }

    public static MaterialCompositionManager getInstance() {
        return INSTANCE;
    }

    @Override
    public Map<Material, Composition> getCompositions() {
        return this.compositions;
    }

    @Override
    public List<Material> getComposed() {
        return this.composed;
    }

    @Override
    public void clearData() {
        this.compositions.clear();
        this.composed.clear();
    }

    @Override
    public void putComposition(Material composed, Composition composition) {
        this.compositions.put(composed, composition);
        this.composed.add(composed);
    }

    @Override
    public Material getFromKey(ResourceLocation key) {
        return RutileRegistries.MATERIALS.get(key);
    }
}
