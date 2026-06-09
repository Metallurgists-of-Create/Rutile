package dev.metallurgists.rutile.registry;

import dev.metallurgists.rutile.api.part.Part;
import dev.metallurgists.rutile.api.part.PartKey;
import dev.metallurgists.rutile.api.registry.RutileRegistry;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.*;

public class RutileParts {

    private static final Map<PartKey<?>, Part<?>> parts = new Object2ObjectOpenHashMap<>();

    public static <T> PartKey<T> registerPart(String name, Part<T> defaultPart) {
        PartKey<T> key = new PartKey<>(defaultPart, name);

        //if (RutileRegistries.PARTS_REGISTRY.containsKey(key)) throw new IllegalArgumentException("Part %s already exists!".formatted(name));
        //RutileRegistries.PARTS_REGISTRY.
        return key;
    }
}
