package dev.metallurgists.rutile.api.material.component;


import net.minecraft.core.component.PatchedDataComponentMap;

import java.util.Map;

public record TypedMaterialComponent<T>(MaterialComponentType<T> type, T value) {

    static TypedMaterialComponent<?> fromEntryUnchecked(Map.Entry<MaterialComponentType<?>, Object> entry) {
        return createUnchecked((MaterialComponentType)entry.getKey(), entry.getValue());
    }

    public static <T> TypedMaterialComponent<T> createUnchecked(MaterialComponentType<T> type, Object value) {
        return new TypedMaterialComponent<>(type, (T) value);
    }

    public void applyTo(PatchedMaterialComponentMap map) {
        map.set(this.type, this.value);
    }

    public String toString() {
        String var10000 = String.valueOf(this.type);
        return var10000 + "=>" + String.valueOf(this.value);
    }
}
