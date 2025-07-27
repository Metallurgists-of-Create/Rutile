package dev.metallurgists.rutile.common;

import dev.metallurgists.rutile.api.composition.tooltip.CompositionManager;
import dev.metallurgists.rutile.api.composition.tooltip.MaterialCompositionManager;
import dev.metallurgists.rutile.api.material.events.PostMaterialEvent;
import dev.metallurgists.rutile.api.registry.RutileRegistries;
import dev.metallurgists.rutile.registry.RutileMaterials;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CommonEventHandler {

    @SubscribeEvent
    public void jsonReading(AddReloadListenerEvent event) {
        RutileRegistries.updateFrozenRegistry(event.getRegistryAccess());

        CompositionManager compositionManager = new CompositionManager();
        MaterialCompositionManager materialCompositionManager = new MaterialCompositionManager();
        event.addListener(compositionManager);
        event.addListener(materialCompositionManager);
    }


}
