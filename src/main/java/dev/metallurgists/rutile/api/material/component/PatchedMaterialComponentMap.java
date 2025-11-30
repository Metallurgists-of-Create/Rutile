package dev.metallurgists.rutile.api.material.component;

import com.google.common.math.IntMath;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.core.component.*;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PatchedMaterialComponentMap implements MaterialComponentMap {
    private final MaterialComponentMap prototype;
    private Reference2ObjectMap<MaterialComponentType<?>, Optional<?>> patch;
    private boolean copyOnWrite;

    public PatchedMaterialComponentMap(MaterialComponentMap prototype) {
        this(prototype, Reference2ObjectMaps.emptyMap(), true);
    }

    private PatchedMaterialComponentMap(MaterialComponentMap prototype, Reference2ObjectMap<MaterialComponentType<?>, Optional<?>> patch, boolean copyOnWtite) {
        this.prototype = prototype;
        this.patch = patch;
        this.copyOnWrite = copyOnWtite;
    }

    public static PatchedMaterialComponentMap fromPatch(MaterialComponentMap prototype, MaterialComponentPatch patch) {
        if (isPatchSanitized(prototype, patch.map)) {
            return new PatchedMaterialComponentMap(prototype, patch.map, true);
        } else {
            PatchedMaterialComponentMap patchedcomponentmap = new PatchedMaterialComponentMap(prototype);
            patchedcomponentmap.applyPatch(patch);
            return patchedcomponentmap;
        }
    }

    private static boolean isPatchSanitized(MaterialComponentMap prototype, Reference2ObjectMap<MaterialComponentType<?>, Optional<?>> map) {

        for (Map.Entry<MaterialComponentType<?>, Optional<?>> entry : Reference2ObjectMaps.fastIterable(map)) {
            Object object = prototype.get((MaterialComponentType<?>) entry.getKey());
            Optional<?> optional = entry.getValue();
            if (optional.isPresent() && optional.get().equals(object)) {
                return false;
            }

            if (optional.isEmpty() && object == null) {
                return false;
            }
        }

        return true;
    }

    @Nullable
    public <T> T get(MaterialComponentType<? extends T> component) {
        Optional<? extends T> optional = (Optional<? extends T>)this.patch.get(component);
        return (T)(optional.isPresent() ? optional.orElse(null) : this.prototype.get(component));
    }

    @Nullable
    public <T> T set(MaterialComponentType<? super T> component, @Nullable T value) {
        this.ensureMapOwnership();
        T t = (T)this.prototype.get(component);
        Optional<T> optional;
        if (Objects.equals(value, t)) {
            optional = (Optional<T>)this.patch.remove(component);
        } else {
            optional = (Optional<T>)this.patch.put(component, Optional.ofNullable(value));
        }

        return (T)(optional.isPresent() ? optional.orElse(t) : t);
    }

    @Nullable
    public <T> T remove(MaterialComponentType<? extends T> component) {
        this.ensureMapOwnership();
        T t = (T)this.prototype.get(component);
        Optional<? extends T> optional;
        if (t != null) {
            optional = (Optional<? extends T>) this.patch.put(component, Optional.empty());
        } else {
            optional = (Optional<? extends T>) this.patch.remove(component);
        }

        return (T)(optional.isPresent() ? optional.orElse(null) : t);
    }

    public void applyPatch(MaterialComponentPatch patch) {
        this.ensureMapOwnership();

        for (Reference2ObjectMap.Entry<MaterialComponentType<?>, Optional<?>> entry : Reference2ObjectMaps.fastIterable(patch.map)) {
            this.applyPatch(entry.getKey(), entry.getValue());
        }

    }

    private void applyPatch(MaterialComponentType<?> component, Optional<?> value) {
        Object object = this.prototype.get(component);
        if (value.isPresent()) {
            if (value.get().equals(object)) {
                this.patch.remove(component);
            } else {
                this.patch.put(component, value);
            }
        } else if (object != null) {
            this.patch.put(component, Optional.empty());
        } else {
            this.patch.remove(component);
        }

    }

    public void restorePatch(MaterialComponentPatch patch) {
        this.ensureMapOwnership();
        this.patch.clear();
        this.patch.putAll(patch.map);
    }

    public void setAll(MaterialComponentMap map) {
        for(TypedMaterialComponent<?> typedComponent : map) {
            typedComponent.applyTo(this);
        }

    }

    private void ensureMapOwnership() {
        if (this.copyOnWrite) {
            this.patch = new Reference2ObjectArrayMap<>(this.patch);
            this.copyOnWrite = false;
        }

    }

    public Set<MaterialComponentType<?>> keySet() {
        if (this.patch.isEmpty()) {
            return this.prototype.keySet();
        } else {
            Set<MaterialComponentType<?>> set = new ReferenceArraySet<>(this.prototype.keySet());

            for (Reference2ObjectMap.Entry<MaterialComponentType<?>, Optional<?>> entry : Reference2ObjectMaps.fastIterable(this.patch)) {
                Optional<?> optional = entry.getValue();
                if (optional.isPresent()) {
                    set.add(entry.getKey());
                } else {
                    set.remove(entry.getKey());
                }
            }

            return set;
        }
    }

    public Iterator<TypedMaterialComponent<?>> iterator() {
        if (this.patch.isEmpty()) {
            return this.prototype.iterator();
        } else {
            List<TypedMaterialComponent<?>> list = new ArrayList<>(this.patch.size() + this.prototype.size());

            for (Reference2ObjectMap.Entry<MaterialComponentType<?>, Optional<?>> entry : Reference2ObjectMaps.fastIterable(this.patch)) {
                if (entry.getValue().isPresent()) {
                    list.add(TypedMaterialComponent.createUnchecked(entry.getKey(), entry.getValue().get()));
                }
            }

            for(TypedMaterialComponent<?> typedMaterialComponent : this.prototype) {
                if (!this.patch.containsKey(typedMaterialComponent.type())) {
                    list.add(typedMaterialComponent);
                }
            }

            return list.iterator();
        }
    }

    public int size() {
        int i = this.prototype.size();

        for (Reference2ObjectMap.Entry<MaterialComponentType<?>, Optional<?>> entry : Reference2ObjectMaps.fastIterable(this.patch)) {
            boolean flag = entry.getValue().isPresent();
            boolean flag1 = this.prototype.has(entry.getKey());
            if (flag != flag1) {
                i += flag ? 1 : -1;
            }
        }

        return i;
    }

    public boolean isPatchEmpty() {
        return this.patch.isEmpty();
    }

    public MaterialComponentPatch asPatch() {
        if (this.patch.isEmpty()) {
            return MaterialComponentPatch.EMPTY;
        } else {
            this.copyOnWrite = true;
            return new MaterialComponentPatch(this.patch);
        }
    }

    public PatchedMaterialComponentMap copy() {
        this.copyOnWrite = true;
        return new PatchedMaterialComponentMap(this.prototype, this.patch, true);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else {
            if (other instanceof PatchedMaterialComponentMap patchedMaterialComponentMap) {
                if (this.prototype.equals(patchedMaterialComponentMap.prototype) && this.patch.equals(patchedMaterialComponentMap.patch)) {
                    return true;
                }
            }

            return false;
        }
    }

    public int hashCode() {
        return this.prototype.hashCode() + hashPatch(this.patch) * 31;
    }

    private static int hashPatch(Reference2ObjectMap<MaterialComponentType<?>, Optional<?>> patch) {
        int h = 0;
        int n = patch.size();

        int entryHash;
        for(ObjectIterator<Reference2ObjectMap.Entry<MaterialComponentType<?>, Optional<?>>> iterator = Reference2ObjectMaps.fastIterator(patch); n-- != 0; h += entryHash) {
            Reference2ObjectMap.Entry<MaterialComponentType<?>, Optional<?>> entry = iterator.next();
            int exponent = System.identityHashCode(entry.getKey()) & 255;
            entryHash = IntMath.pow(31, exponent) * (entry.getValue()).hashCode();
        }

        return h;
    }

    public String toString() {
        Stream var10000 = this.stream().map(TypedMaterialComponent::toString);
        return "{" + var10000.collect(Collectors.joining(", ")) + "}";
    }
}
