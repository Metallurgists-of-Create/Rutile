package dev.metallurgists.rutile.api.registry;

import dev.metallurgists.rutile.api.material.Material;
import lombok.Getter;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;

public class PostRutileRegistryEvent<T> extends Event implements IModBusEvent {
    public static final PostRutileRegistryEvent<Material> MATERIAL = new PostRutileRegistryEvent<>(RutileRegistries.MATERIAL_REGISTRY);

    @Getter
    private final ResourceKey<Registry<T>> registryKey;

    public PostRutileRegistryEvent(ResourceKey<Registry<T>> registryKey) {
        this.registryKey = registryKey;
    }
}
