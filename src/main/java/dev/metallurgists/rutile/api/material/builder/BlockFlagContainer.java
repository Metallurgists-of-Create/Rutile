package dev.metallurgists.rutile.api.material.builder;

import dev.metallurgists.rutile.api.material.FlagRegistryTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class BlockFlagContainer extends FlagContainer<Block> {

    public BlockFlagContainer() {
        super(FlagRegistryTypes.BLOCK);
    }

    @Override
    public final Registry<Block> getRegistry() {
        return BuiltInRegistries.BLOCK;
    }

    @Override
    public Block get(ResourceKey<Block> registryKey) {
        return getRegistry().get(registryKey);
    }

    public Item getItem(ResourceLocation location) {
        return getItem(ResourceKey.create(Registries.ITEM, location));
    }

    public Item getItem(ResourceKey<Item> registryKey) {
        return BuiltInRegistries.ITEM.get(registryKey);
    }
}
