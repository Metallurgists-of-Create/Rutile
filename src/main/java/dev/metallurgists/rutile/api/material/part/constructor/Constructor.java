package dev.metallurgists.rutile.api.material.part.constructor;

import dev.metallurgists.rutile.api.material.MaterialData;
import dev.metallurgists.rutile.api.material.part.PartKey;
import net.minecraft.world.item.Item;

@FunctionalInterface
public interface Constructor<T, I> {
    T create(I properties, PartKey<T> partKey, MaterialData data);
}

