package dev.metallurgists.rutile.api.material.component;

import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMaps;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

@SuppressWarnings("unchecked")
public final class MaterialComponentPatch {
    public static final MaterialComponentPatch EMPTY = new MaterialComponentPatch(Reference2ObjectMaps.emptyMap());
    private static final String REMOVED_PREFIX = "!";
    final Reference2ObjectMap<MaterialComponentType<?>, Optional<?>> map;

    MaterialComponentPatch(Reference2ObjectMap<MaterialComponentType<?>, Optional<?>> map) {
        this.map = map;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Nullable
    public <T> Optional<? extends T> get(MaterialComponentType<? extends T> component) {
        return (Optional<? extends T>)this.map.get(component);
    }

    public Set<Map.Entry<MaterialComponentType<?>, Optional<?>>> entrySet() {
        return this.map.entrySet();
    }

    public int size() {
        return this.map.size();
    }

    public MaterialComponentPatch forget(Predicate<MaterialComponentType<?>> predicate) {
        if (this.isEmpty()) {
            return EMPTY;
        } else {
            Reference2ObjectMap<MaterialComponentType<?>, Optional<?>> reference2objectmap = new Reference2ObjectArrayMap<>(this.map);
            reference2objectmap.keySet().removeIf(predicate);
            return reference2objectmap.isEmpty() ? EMPTY : new MaterialComponentPatch(reference2objectmap);
        }
    }

    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    public SplitResult split() {
        if (this.isEmpty()) {
            return SplitResult.EMPTY;
        } else {
            MaterialComponentMap.Builder componentMap$builder = MaterialComponentMap.builder();
            Set<MaterialComponentType<?>> set = Sets.newIdentityHashSet();
            this.map.forEach((componentType, value) -> {
                if (value.isPresent()) {
                    componentMap$builder.setUnchecked(componentType, value.get());
                } else {
                    set.add(componentType);
                }

            });
            return new SplitResult(componentMap$builder.build(), set);
        }
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else {
            if (other instanceof MaterialComponentPatch componentPatch) {
                return this.map.equals(componentPatch.map);
            }

            return false;
        }
    }

    public int hashCode() {
        return this.map.hashCode();
    }

    public String toString() {
        return toString(this.map);
    }

    static String toString(Reference2ObjectMap<MaterialComponentType<?>, Optional<?>> map) {
        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append('{');
        boolean flag = true;

        for (Map.Entry<MaterialComponentType<?>, Optional<?>> entry : Reference2ObjectMaps.fastIterable(map)) {
            if (flag) {
                flag = false;
            } else {
                stringbuilder.append(", ");
            }

            Optional<?> optional = entry.getValue();
            if (optional.isPresent()) {
                stringbuilder.append(entry.getKey());
                stringbuilder.append("=>");
                stringbuilder.append(optional.get());
            } else {
                stringbuilder.append("!");
                stringbuilder.append(entry.getKey());
            }
        }

        stringbuilder.append('}');
        return stringbuilder.toString();
    }

    public static class Builder {
        private final Reference2ObjectMap<MaterialComponentType<?>, Optional<?>> map = new Reference2ObjectArrayMap<>();

        Builder() {
        }

        public <T> Builder set(MaterialComponentType<T> component, T value) {
            this.map.put(component, Optional.of(value));
            return this;
        }

        public <T> Builder remove(MaterialComponentType<T> component) {
            this.map.put(component, Optional.empty());
            return this;
        }

        public <T> Builder set(TypedMaterialComponent<T> component) {
            return this.set(component.type(), component.value());
        }

        public MaterialComponentPatch build() {
            return this.map.isEmpty() ? MaterialComponentPatch.EMPTY : new MaterialComponentPatch(this.map);
        }
    }

    public static record SplitResult(MaterialComponentMap added, Set<MaterialComponentType<?>> removed) {
        public static final SplitResult EMPTY;

        static {
            EMPTY = new SplitResult(MaterialComponentMap.EMPTY, Set.of());
        }
    }
}
