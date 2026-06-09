package dev.metallurgists.rutile.api.part.type;

import dev.metallurgists.rutile.api.material.objects.MaterialItem;
import dev.metallurgists.rutile.api.part.Part;
import dev.metallurgists.rutile.api.part.constructor.ItemConstructor;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public abstract class ItemPart extends Part<Item> {

    @Override
    public ItemConstructor constructor() {
        return MaterialItem::new;
    }

    @Override
    public ResourceKey<Registry<Item>> registryResourceKey() {
        return Registries.ITEM;
    }
}
