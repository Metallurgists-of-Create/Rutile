package dev.metallurgists.rutile.api.material.part.constructor;

import dev.metallurgists.rutile.api.material.MaterialData;
import dev.metallurgists.rutile.api.material.part.PartKey;
import net.minecraft.world.item.Item;

@FunctionalInterface
public interface ItemConstructor {
    Item create(Item.Properties properties, PartKey<Item> partKey, MaterialData data);
}
