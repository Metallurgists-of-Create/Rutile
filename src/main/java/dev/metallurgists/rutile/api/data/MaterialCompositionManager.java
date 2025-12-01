package dev.metallurgists.rutile.api.data;

import dev.metallurgists.rutile.api.RutileApi;
import dev.metallurgists.rutile.api.composition.Composition;
import dev.metallurgists.rutile.api.material.Material;
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
        super("material");
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
        return RutileApi.getMaterialRegistry().getById(key);
    }

    public static void register(AddReloadListenerEvent event) {
        event.addListener(RutileApi.getMaterialCompositionManager());
    }
}
