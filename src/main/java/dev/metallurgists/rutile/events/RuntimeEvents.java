package dev.metallurgists.rutile.events;

import dev.metallurgists.rutile.api.data.manager.composition.ItemCompositionManager;
import dev.metallurgists.rutile.api.data.manager.composition.MaterialCompositionManager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

@EventBusSubscriber
public class RuntimeEvents {

    @SubscribeEvent
    public static void registerListeners(AddReloadListenerEvent event) {
        ItemCompositionManager.register(event);
        MaterialCompositionManager.register(event);
    }
}
