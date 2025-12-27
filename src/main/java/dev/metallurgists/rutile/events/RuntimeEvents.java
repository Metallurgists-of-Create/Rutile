package dev.metallurgists.rutile.events;

import dev.metallurgists.rutile.api.composition.RutileCompositions;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

@EventBusSubscriber
public class RuntimeEvents {

    @SubscribeEvent
    public static void registerListeners(AddReloadListenerEvent event) {
        RutileCompositions.INSTANCE.register(event);
    }
}
