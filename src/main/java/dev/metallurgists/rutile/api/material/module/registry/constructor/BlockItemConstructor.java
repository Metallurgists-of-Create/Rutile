package dev.metallurgists.rutile.api.material.module.registry.constructor;

import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.material.module.registry.RegistryModule;
import net.minecraft.core.Holder;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

@FunctionalInterface
public interface BlockItemConstructor {
    BlockItem create(Block block, Item.Properties properties, Holder<RegistryModule.Key> registerKey, Material material);
}
