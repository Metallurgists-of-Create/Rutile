package dev.metallurgists.rutile.api.material.part.constructor;

import net.minecraft.world.item.Item;

@FunctionalInterface
public interface ItemConstructor extends Constructor<Item, Item.Properties> {
}
