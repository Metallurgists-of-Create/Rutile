package dev.metallurgists.rutile.registry;

import com.mojang.serialization.Lifecycle;
import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.element.Element;
import dev.metallurgists.rutile.api.material.module.MaterialModule;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

@EventBusSubscriber
public class RutileRegistries {
    public static final ResourceKey<Registry<Element>> ELEMENTS = createRegistryKey("element");
    public static final ResourceKey<Registry<MaterialModule<?>>> MODULES = createRegistryKey("modules");

    public static final Registry<Element> ELEMENTS_REGISTRY = makeSyncedRegistry(ELEMENTS);
    public static final Registry<MaterialModule<?>> MODULES_REGISTRY = makeSyncedRegistry(MODULES);

    private static <T> ResourceKey<Registry<T>> createRegistryKey(String name) {
        return ResourceKey.createRegistryKey(Rutile.getResource(name));
    }
    /**
     * Creates a {@link Registry} that get synchronised to clients.
     *
     * @param <T> the entry of the registry.
     */
    private static <T> Registry<T> makeSyncedRegistry(ResourceKey<Registry<T>> registryKey) {
        return new RegistryBuilder<>(registryKey).sync(true).create();
    }
    /**
     * Creates a simple {@link Registry} that <B>won't</B> be synced to clients.
     *
     * @param <T> the entry of the registry.
     */
    private static <T> Registry<T> makeRegistry(ResourceKey<Registry<T>> registryKey) {
        return new RegistryBuilder<>(registryKey).create();
    }
    private static <T> Registry<T> registerSimpleWithIntrusiveHolders(ResourceKey<? extends Registry<T>> registryKey) {
        return new MappedRegistry<>(registryKey, Lifecycle.stable(), true);
    }


    @SubscribeEvent
    public static void newRegistry(NewRegistryEvent event) {
        event.register(ELEMENTS_REGISTRY);
        event.register(MODULES_REGISTRY);
    }
}
