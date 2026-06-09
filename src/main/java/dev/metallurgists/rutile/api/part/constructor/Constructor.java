package dev.metallurgists.rutile.api.part.constructor;

import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.part.PartKey;

@FunctionalInterface
public interface Constructor<T, I> {
    T create(I properties, PartKey<T> partKey, Material data);
}

