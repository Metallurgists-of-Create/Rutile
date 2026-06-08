package dev.metallurgists.rutile.api.material.module;

public interface MaterialModule<T extends MaterialModule<T>> {

    T getType();

    ModuleBuilder<T> builder();

    interface ModuleBuilder<T extends MaterialModule<T>> {

        T build();
    }
}
