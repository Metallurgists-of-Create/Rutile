package dev.metallurgists.rutile.api.material;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.material.builder.FlagSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public class FlagSources {
    public static final FlagSource<Item>
            INGOT = item("ingot"),
            NUGGET = item("nugget"),
            DUST = item("dust"),
            GEM = item("gem")
                    ;

    public static final FlagSource<Block>
            STORAGE_BLOCK = block("storage_block")
            ;

    public static final FlagSource<Fluid>
            MOLTEN = fluid("molten")
            ;

    public static FlagSource<Item> item(String name) {
        return new FlagSource<>(Rutile.id(name), FlagRegistryTypes.ITEM);
    }

    public static FlagSource<Block> block(String name) {
        return new FlagSource<>(Rutile.id(name), FlagRegistryTypes.BLOCK);
    }

    public static FlagSource<Fluid> fluid(String name) {
        return new FlagSource<>(Rutile.id(name), FlagRegistryTypes.FLUID);
    }
}
