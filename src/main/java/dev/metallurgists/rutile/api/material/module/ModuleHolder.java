package dev.metallurgists.rutile.api.material.module;

import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModuleHolder<T extends MaterialModule<?>> extends DeferredHolder<MaterialModule<?>, T> {

    protected ModuleHolder(ResourceKey<MaterialModule<?>> key) {
        super(key);
    }
}
