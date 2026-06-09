package dev.metallurgists.rutile.api.material.module;

import dev.metallurgists.rutile.registry.deferred.ModuleHolder;

public interface MaterialModule<T extends MaterialModule<T>> {

    ModuleHolder<T> getType();

    ModuleBuilder<T> builder();

    interface ModuleBuilder<T extends MaterialModule<T>> {

        T build();
    }
}
