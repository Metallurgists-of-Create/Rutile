package dev.metallurgists.rutile.registry;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.composition.element.Element;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

@EventBusSubscriber
public class RutileRegistries {
    public static final ResourceKey<Registry<Element>> ELEMENTS = createRegistryKey("element");

    public static final Registry<Element> ELEMENTS_REGISTRY = makeSyncedRegistry(ELEMENTS);

    private static <T> ResourceKey<Registry<T>> createRegistryKey(String name) {
        return ResourceKey.createRegistryKey(Rutile.id(name));
    }

    private static <T> Registry<T> makeSyncedRegistry(ResourceKey<Registry<T>> registryKey) {
        return new RegistryBuilder<>(registryKey).sync(true).create();
    }

    @SubscribeEvent
    public static void newRegistry(NewRegistryEvent event) {
        event.register(ELEMENTS_REGISTRY);
    }
}
