package dev.metallurgists.rutile.api.part.constructor;

import net.minecraft.world.item.Item;

@FunctionalInterface
public interface ItemConstructor extends Constructor<Item, Item.Properties> {
}
