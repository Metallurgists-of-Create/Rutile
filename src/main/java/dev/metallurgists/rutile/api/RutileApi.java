package dev.metallurgists.rutile.api;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.data.server.manager.composition.ItemCompositionManager;
import dev.metallurgists.rutile.api.data.server.manager.composition.MaterialCompositionManager;
import dev.metallurgists.rutile.api.element.Element;
import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.registry.IRutileRegistry;
import dev.metallurgists.rutile.api.tag.TagPrefix;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.Getter;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class RutileApi {
    public static final String ID = "rutile";

    public static Rutile instance;

    public static Int2ObjectOpenHashMap<TagKey<Block>> harvestLevels = Util.make(new Int2ObjectOpenHashMap<>(), map -> {
        map.put(0, BlockTags.INCORRECT_FOR_WOODEN_TOOL);
        map.put(1, BlockTags.INCORRECT_FOR_STONE_TOOL);
        map.put(2, BlockTags.INCORRECT_FOR_IRON_TOOL);
        map.put(3, BlockTags.INCORRECT_FOR_DIAMOND_TOOL);
        map.put(4, BlockTags.INCORRECT_FOR_NETHERITE_TOOL);
    });

    /**
     * Creates a {@link ResourceLocation} in the `rutile` namespace
     * @param path the path
     * @return the resource location
     */
    public static ResourceLocation resource(String path) {
        return ResourceLocation.fromNamespaceAndPath(ID, path);
    }
}
