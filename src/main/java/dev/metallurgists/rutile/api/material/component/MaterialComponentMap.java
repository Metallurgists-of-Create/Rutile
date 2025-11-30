package dev.metallurgists.rutile.api.material.component;

import com.google.common.collect.Iterators;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMaps;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.TypedDataComponent;
import net.neoforged.neoforge.common.extensions.IDataComponentMapBuilderExtensions;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface MaterialComponentMap extends Iterable<TypedMaterialComponent<?>> {
    MaterialComponentMap EMPTY = new MaterialComponentMap() {
        @Nullable
        public <T> T get(MaterialComponentType<? extends T> type) {
            return null;
        }

        public Set<MaterialComponentType<?>> keySet() {
            return Set.of();
        }

        public Iterator<TypedMaterialComponent<?>> iterator() {
            return Collections.emptyIterator();
        }
    };

    static MaterialComponentMap composite(final MaterialComponentMap map1, final MaterialComponentMap map2) {
        return new MaterialComponentMap() {
            @Nullable
            public <T> T get(MaterialComponentType<? extends T> type) {
                T t = (T)map2.get(type);
                return (T)(t != null ? t : map1.get(type));
            }

            public Set<MaterialComponentType<?>> keySet() {
                return Sets.union(map1.keySet(), map2.keySet());
            }
        };
    }

    static Builder builder() {
        return new Builder();
    }

    @Nullable
    <T> T get(MaterialComponentType<? extends T> var1);

    Set<MaterialComponentType<?>> keySet();

    default boolean has(MaterialComponentType<?> component) {
        return this.get(component) != null;
    }

    default <T> T getOrDefault(MaterialComponentType<? extends T> component, T defaultValue) {
        T t = (T)this.get(component);
        return (T)(t != null ? t : defaultValue);
    }

    @Nullable
    default <T> TypedMaterialComponent<T> getTyped(MaterialComponentType<T> component) {
        T t = (T)this.get(component);
        return t != null ? new TypedMaterialComponent<>(component, t) : null;
    }

    default Iterator<TypedMaterialComponent<?>> iterator() {
        return Iterators.transform(this.keySet().iterator(), (type) -> Objects.requireNonNull(this.getTyped(type)));
    }

    default Stream<TypedMaterialComponent<?>> stream() {
        return StreamSupport.stream(Spliterators.spliterator(this.iterator(), this.size(), 1345), false);
    }

    default int size() {
        return this.keySet().size();
    }

    default boolean isEmpty() {
        return this.size() == 0;
    }

    default MaterialComponentMap filter(final Predicate<MaterialComponentType<?>> predicate) {
        return new MaterialComponentMap() {
            @Nullable
            public <T> T get(MaterialComponentType<? extends T> type) {
                return (T)(predicate.test(type) ? MaterialComponentMap.this.get(type) : null);
            }

            public Set<MaterialComponentType<?>> keySet() {
                Set<MaterialComponentType<?>> keySet = MaterialComponentMap.this.keySet();
                Objects.requireNonNull(predicate);
                return Sets.filter(keySet, predicate::test);
            }
        };
    }

    public static class Builder implements IDataComponentMapBuilderExtensions {
        private final Reference2ObjectMap<MaterialComponentType<?>, Object> map = new Reference2ObjectArrayMap<>();

        Builder() {
        }

        public <T> Builder set(MaterialComponentType<T> component, @Nullable T value) {
            this.setUnchecked(component, value);
            return this;
        }

        <T> void setUnchecked(MaterialComponentType<T> component, @Nullable Object value) {
            if (value != null) {
                this.map.put(component, value);
            } else {
                this.map.remove(component);
            }

        }

        public Builder addAll(MaterialComponentMap components) {
            for(TypedMaterialComponent<?> typedMaterialComponent : components) {
                this.map.put(typedMaterialComponent.type(), typedMaterialComponent.value());
            }

            return this;
        }

        public MaterialComponentMap build() {
            return buildFromMapTrusted(this.map);
        }

        private static MaterialComponentMap buildFromMapTrusted(Map<MaterialComponentType<?>, Object> map) {
            if (map.isEmpty()) {
                return MaterialComponentMap.EMPTY;
            } else {
                return map.size() < 8 ? new SimpleMap(new Reference2ObjectArrayMap<>(map)) : new SimpleMap(new Reference2ObjectOpenHashMap<>(map));
            }
        }

        static record SimpleMap(Reference2ObjectMap<MaterialComponentType<?>, Object> map) implements MaterialComponentMap {
            @Nullable
            public <T> T get(MaterialComponentType<? extends T> type) {
                return (T)this.map.get(type);
            }

            public boolean has(MaterialComponentType<?> type) {
                return this.map.containsKey(type);
            }

            public Set<MaterialComponentType<?>> keySet() {
                return this.map.keySet();
            }

            public Iterator<TypedMaterialComponent<?>> iterator() {
                return Iterators.transform(Reference2ObjectMaps.fastIterator(this.map), entry -> entry != null ? TypedMaterialComponent.fromEntryUnchecked(entry) : null);
            }

            public int size() {
                return this.map.size();
            }

            public String toString() {
                return this.map.toString();
            }
        }
    }
}
