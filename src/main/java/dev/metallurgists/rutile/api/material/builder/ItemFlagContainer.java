package dev.metallurgists.rutile.api.material.builder;

import dev.metallurgists.rutile.api.material.FlagRegistryTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public class ItemFlagContainer extends FlagContainer<Item> {

    public ItemFlagContainer() {
        super(FlagRegistryTypes.ITEM);
    }

    @Override
    public final Registry<Item> getRegistry() {
        return BuiltInRegistries.ITEM;
    }

    @Override
    public Item get(ResourceKey<Item> registryKey) {
        return getRegistry().get(registryKey);
    }
}
