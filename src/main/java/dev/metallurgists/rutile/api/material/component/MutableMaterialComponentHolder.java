package dev.metallurgists.rutile.api.material.component;

import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public interface MutableMaterialComponentHolder extends MaterialComponentHolder {
    <T> @Nullable T set(MaterialComponentType<? super T> var1, @Nullable T var2);

    default <T> @Nullable T set(Supplier<? extends MaterialComponentType<? super T>> componentType, @Nullable T value) {
        return (T)this.set(componentType.get(), value);
    }

    default <T, U> @Nullable T update(MaterialComponentType<T> componentType, T value, U updateContext, BiFunction<T, U, T> updater) {
        return (T)this.set(componentType, updater.apply(this.getOrDefault(componentType, value), updateContext));
    }

    default <T, U> @Nullable T update(Supplier<? extends MaterialComponentType<T>> componentType, T value, U updateContext, BiFunction<T, U, T> updater) {
        return (T)this.update(componentType.get(), value, updateContext, updater);
    }

    default <T> @Nullable T update(MaterialComponentType<T> componentType, T value, UnaryOperator<T> updater) {
        return (T)this.set(componentType, updater.apply(this.getOrDefault(componentType, value)));
    }

    default <T> @Nullable T update(Supplier<? extends MaterialComponentType<T>> componentType, T value, UnaryOperator<T> updater) {
        return (T)this.update(componentType.get(), value, updater);
    }

    <T> @Nullable T remove(MaterialComponentType<? extends T> var1);

    default <T> @Nullable T remove(Supplier<? extends MaterialComponentType<? extends T>> componentType) {
        return (T)this.remove(componentType.get());
    }

    default void copyFrom(MaterialComponentHolder src, MaterialComponentType<?>... componentTypes) {
        for(MaterialComponentType<?> componentType : componentTypes) {
            this.copyFrom(componentType, src);
        }

    }

    default void copyFrom(MaterialComponentHolder src, Supplier<? extends MaterialComponentType<?>>... componentTypes) {
        for(Supplier<? extends MaterialComponentType<?>> componentType : componentTypes) {
            this.copyFrom(componentType.get(), src);
        }

    }

    void applyComponents(MaterialComponentPatch var1);

    void applyComponents(MaterialComponentMap var1);

    private <T> void copyFrom(MaterialComponentType<T> componentType, MaterialComponentHolder src) {
        this.set(componentType, src.get(componentType));
    }
}
