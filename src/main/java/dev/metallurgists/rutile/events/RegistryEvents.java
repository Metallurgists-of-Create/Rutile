package dev.metallurgists.rutile.events;

import dev.metallurgists.rutile.api.composition.data.ItemComposition;
import dev.metallurgists.rutile.api.composition.data.MaterialComposition;
import dev.metallurgists.rutile.registry.RutileRegistries;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;

public class RegistryEvents {

    @SubscribeEvent
    public void newDatapackRegistry(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(RutileRegistries.ITEM_COMPOSITION, ItemComposition.CODEC, ItemComposition.CODEC);
        event.dataPackRegistry(RutileRegistries.MATERIAL_COMPOSITION, MaterialComposition.CODEC, MaterialComposition.CODEC);
    }

    @SubscribeEvent
    public void newRegistry(NewRegistryEvent event) {
        event.register(RutileRegistries.FLAG_KEY_REGISTRY);
        event.register(RutileRegistries.ELEMENT_REGISTRY);
        event.register(RutileRegistries.MATERIAL_REGISTRY);
    }
}
