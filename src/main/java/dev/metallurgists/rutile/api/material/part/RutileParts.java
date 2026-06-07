package dev.metallurgists.rutile.api.material.part;

import java.util.HashMap;
import java.util.Map;

public class RutileParts {

    private static final Map<String, PartKey<?>> keys = new HashMap<>();

    public static <T> PartKey<T> registerPart(String name, Part<T> defaultPart) {
        if (keys.containsKey(name)) throw new IllegalArgumentException("Part %s already exists!".formatted(name));
        PartKey<T> key = new PartKey<>(defaultPart, name);
        keys.put(name, key);
        return key;
    }
}
