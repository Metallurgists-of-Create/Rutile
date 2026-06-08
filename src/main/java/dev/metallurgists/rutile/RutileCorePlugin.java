package dev.metallurgists.rutile;

import dev.metallurgists.rutile.api.composition.Composition;
import dev.metallurgists.rutile.api.composition.RutileCompositions;
import dev.metallurgists.rutile.api.data.server.manager.composition.FluidCompositionManager;
import dev.metallurgists.rutile.api.data.server.manager.composition.ItemCompositionManager;
import dev.metallurgists.rutile.api.material.RutileMaterial;
import dev.metallurgists.rutile.api.plugin.IRutilePlugin;
import dev.metallurgists.rutile.api.plugin.PluginConfig;
import dev.metallurgists.rutile.registry.RutileElements;

public class RutileCorePlugin implements IRutilePlugin {
    @Override
    public void configure(PluginConfig config) {
        config.setModId(Rutile.ID);
        config.setRegistrate(Rutile.getRegistrate());
    }

    @Override
    public void collectCompositionManagers(RutileCompositions handler) {
        handler.addManager(ItemCompositionManager.getInstance());
        handler.addManager(FluidCompositionManager.getInstance());
    }

    @Override
    public void modifyMaterials(RutileMaterial materials) {
        materials.getOrCreate("iron").modify(data -> data.setComposition(Composition.builder()
                .element(RutileElements.Fe)
                .end()));
        materials.getOrCreate("copper").modify(data -> data.setComposition(Composition.builder()
                .element(RutileElements.Cu)
                .end()));
        materials.getOrCreate("gold").modify(data -> data.setComposition(Composition.builder()
                .element(RutileElements.Au)
                .end()));
        materials.getOrCreate("diamond").modify(data -> data.setComposition(Composition.builder()
                .element(RutileElements.C)
                .end()));
        materials.getOrCreate("emerald").modify(data -> data.setComposition(Composition.builder()
                .element(RutileElements.Be, 3)
                .element(RutileElements.Al, 2)
                .element(RutileElements.Si, 6)
                .element(RutileElements.O, 18)
                .end()));
        materials.getOrCreate("quartz").modify(data -> data.setComposition(Composition.builder()
                .element(RutileElements.Si)
                .element(RutileElements.O, 2)
                .end()));
        materials.getOrCreate("amethyst").modify(data -> data.setComposition(Composition.builder()
                .element(RutileElements.Si)
                .element(RutileElements.O, 2)
                .element(RutileElements.Fe)
                .end()));
    }
}
