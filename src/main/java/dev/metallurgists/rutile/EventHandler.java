package dev.metallurgists.rutile;

import dev.metallurgists.rutile.api.composition.tooltip.CompositionManager;
import dev.metallurgists.rutile.api.composition.tooltip.MaterialCompositionManager;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventHandler {

    @SubscribeEvent
    public void jsonReading(AddReloadListenerEvent event) {
        CompositionManager compositionManager = new CompositionManager();
        MaterialCompositionManager materialCompositionManager = new MaterialCompositionManager();
        event.addListener(compositionManager);
        event.addListener(materialCompositionManager);
    }
}
