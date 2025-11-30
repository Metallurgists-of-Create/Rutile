package dev.metallurgists.rutile.api.material.builder;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public record FlagRegistryType<T>(ResourceKey<Registry<T>> resourceKey) {
}
