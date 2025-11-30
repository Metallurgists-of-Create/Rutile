package dev.metallurgists.rutile.api.material;

import dev.metallurgists.rutile.api.material.builder.FlagRegistryType;
import dev.metallurgists.rutile.api.material.builder.FlagSource;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public class FlagRegistryTypes {
    public static final FlagRegistryType<Item> ITEM = create(Registries.ITEM);
    public static final FlagRegistryType<Block> BLOCK = create(Registries.BLOCK);
    public static final FlagRegistryType<Fluid> FLUID = create(Registries.FLUID);


    private static <T> FlagRegistryType<T> create(ResourceKey<Registry<T>> resourceKey) {
        return new FlagRegistryType<>(resourceKey);
    }
}
