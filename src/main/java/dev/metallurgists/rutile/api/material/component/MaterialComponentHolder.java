package dev.metallurgists.rutile.api.material.component;

import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public interface MaterialComponentHolder {
    MaterialComponentMap getComponents();

    default <T> @Nullable T get(Supplier<? extends MaterialComponentType<? extends T>> type) {
        return (T)this.get((MaterialComponentType)type.get());
    }

    default <T> T get(MaterialComponentType<? extends T> component) {
        return (T)this.getComponents().get(component);
    }

    default <T> T getOrDefault(Supplier<? extends MaterialComponentType<? extends T>> type, T defaultValue) {
        return (T)this.getOrDefault(type.get(), defaultValue);
    }

    default <T> T getOrDefault(MaterialComponentType<? extends T> component, T defaultValue) {
        return (T)this.getComponents().getOrDefault(component, defaultValue);
    }

    default boolean has(Supplier<? extends MaterialComponentType<?>> type) {
        return this.has(type.get());
    }

    default boolean has(MaterialComponentType<?> component) {
        return this.getComponents().has(component);
    }
}
