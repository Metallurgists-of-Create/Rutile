package dev.metallurgists.rutile.api.material.flags;

import dev.metallurgists.rutile.api.material.Material;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

public class MaterialFlags {

    private final Map<FlagKey<? extends IMaterialFlag>, IMaterialFlag> flagMap;
    @Getter
    @Setter
    private Material material;

    public MaterialFlags() {
        flagMap = new HashMap<>();
    }

    public Collection<FlagKey<? extends IMaterialFlag>> getFlagKeys() {
        return flagMap.keySet();
    }

    public boolean isEmpty() {
        return flagMap.isEmpty();
    }

    public <T extends IMaterialFlag> T getFlag(FlagKey<T> key) {
        return key.cast(flagMap.get(key));
    }

    public <T extends IMaterialFlag> boolean hasFlag(FlagKey<T> key) {
        return flagMap.get(key) != null;
    }

    public <T extends IMaterialFlag> void setFlag(FlagKey<T> key, IMaterialFlag value) {
        if (value == null) throw new IllegalArgumentException("Material Flag must not be null!");
        if (!key.getType().isInstance(value))
            throw new IllegalArgumentException("Material Flag must be of the same type as the flag key!");
        if (hasFlag(key))
            throw new IllegalArgumentException("Material Flag " + key.toString() + " already registered!");
        flagMap.put(key, value);
    }

    public <T extends IMaterialFlag> void removeProperty(FlagKey<T> key) {
        if (!hasFlag(key))
            throw new IllegalArgumentException("Material Property " + key.toString() + " not present!");
        flagMap.remove(key);
    }

    public <T extends IMaterialFlag> void ensureSet(FlagKey<T> key, boolean verify) {
        if (!hasFlag(key)) {
            flagMap.put(key, key.constructDefault());
            if (verify) verify();
        }
    }

    public <T extends IMaterialFlag> void ensureSet(FlagKey<T> key) {
        ensureSet(key, false);
    }

    public void verify() {
        List<IMaterialFlag> oldList;
        do {
            oldList = new ArrayList<>(flagMap.values());
            oldList.forEach(p -> p.verifyFlag(this));
        } while (oldList.size() != flagMap.size());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        flagMap.forEach((k, v) -> sb.append(k.toString()).append("\n"));
        return sb.toString();
    }
}
