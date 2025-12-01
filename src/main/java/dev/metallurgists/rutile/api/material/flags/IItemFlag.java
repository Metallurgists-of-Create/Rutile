package dev.metallurgists.rutile.api.material.flags;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public interface IItemFlag extends IMaterialFlag {
    default ResourceKey<Registry<Item>> getRegistryKey() {
        return Registries.ITEM;
    }
}
