package dev.metallurgists.rutile.api.material.module.registry;

import dev.metallurgists.rutile.api.material.module.MaterialModule;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DeferredHolder;

public class KeyHolder<T extends RegistryModule.Key> extends DeferredHolder<RegistryModule.Key, T> {

    protected KeyHolder(ResourceKey<RegistryModule.Key> key) {
        super(key);
    }
}
