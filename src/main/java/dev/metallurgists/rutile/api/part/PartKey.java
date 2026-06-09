package dev.metallurgists.rutile.api.part;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class PartKey<T> {
    @Getter
    private final Part<T> defaultInstance;
    @Getter
    public final String name;

    private final Map<String, Part<T>> indexed = new HashMap<>();

    public PartKey(Part<T> defaultInstance, String name) {
        this.defaultInstance = defaultInstance;
        this.name = name;
    }

    public void add(String material, Part<T> instance) {
        this.indexed.put(material, instance);
    }

    public void add(String material) {
        this.add(material, defaultInstance);
    }

    public void remove(String material) {
        this.indexed.remove(material);
    }

    public Part<T> getPart(String material) {
        if (!indexed.containsKey(material)) throw new IllegalArgumentException("Material: %s is not indexed to part: %s".formatted(material, getName()));
        return indexed.get(material);
    }
}
