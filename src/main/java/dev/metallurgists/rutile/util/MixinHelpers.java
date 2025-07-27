package dev.metallurgists.rutile.util;

import com.google.common.collect.Table;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.flag.BlockFlag;
import dev.metallurgists.rutile.api.material.flag.FlagKey;
import dev.metallurgists.rutile.api.material.flag.ItemFlag;
import dev.metallurgists.rutile.api.material.registry.block.IMaterialBlock;
import dev.metallurgists.rutile.api.material.registry.block.RutileMaterialBlocks;
import dev.metallurgists.rutile.api.material.registry.item.IMaterialItem;
import dev.metallurgists.rutile.api.material.registry.item.RutileMaterialItems;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MixinHelpers {

    public static <T> void generateDynamicTags(Map<ResourceLocation, List<TagLoader.EntryWithSource>> tagMap, Registry<T> registry) {
        if (registry == BuiltInRegistries.ITEM) {
            for (Table.Cell<FlagKey<?>, Material, ItemEntry<? extends IMaterialItem>> entryCell : RutileMaterialItems.MATERIAL_ITEMS.cellSet()) {
                FlagKey<?> flagKey = entryCell.getRowKey();
                var material = entryCell.getColumnKey();
                var itemEntry = entryCell.getValue();
                if (material != null) {
                    String materialName = material.getName();
                    if (material.hasFlag(flagKey) && material.getFlag(flagKey) instanceof ItemFlag itemFlag) {
                        List<String> tagPatterns = itemFlag.getTagPatterns();
                        for (String tagPattern : tagPatterns) {
                            List<TagLoader.EntryWithSource> tags = new ArrayList<>();
                            String tagName = tagPattern.formatted(materialName);
                            ResourceLocation tagId = ResourceLocation.tryParse(tagName);
                            tags.add(new TagLoader.EntryWithSource(
                                    TagEntry.element(BuiltInRegistries.ITEM.getKey(itemEntry.get())),
                                    "Rutile Custom Tags"));
                            tagMap.computeIfAbsent(tagId, path -> new ArrayList<>()).addAll(tags);
                        }
                    }
                }
            }
            for (Table.Cell<FlagKey<?>, Material, BlockEntry<? extends IMaterialBlock>> entryCell : RutileMaterialBlocks.MATERIAL_BLOCKS.cellSet()) {
                FlagKey<?> flagKey = entryCell.getRowKey();
                var material = entryCell.getColumnKey();
                var itemEntry = entryCell.getValue().asItem();
                if (material != null) {
                    String materialName = material.getName();
                    if (material.hasFlag(flagKey) && material.getFlag(flagKey) instanceof BlockFlag blockFlag) {
                        List<String> tagPatterns = blockFlag.getItemTagPatterns();
                        for (String tagPattern : tagPatterns) {
                            List<TagLoader.EntryWithSource> tags = new ArrayList<>();
                            String tagName = tagPattern.formatted(materialName);
                            ResourceLocation tagId = ResourceLocation.tryParse(tagName);
                            tags.add(new TagLoader.EntryWithSource(
                                    TagEntry.element(BuiltInRegistries.ITEM.getKey(itemEntry)),
                                    "Rutile Custom Tags"));
                            tagMap.computeIfAbsent(tagId, path -> new ArrayList<>()).addAll(tags);
                        }
                    }
                }
            }
        }
        if (registry == BuiltInRegistries.BLOCK) {
            for (Table.Cell<FlagKey<?>, Material, BlockEntry<? extends IMaterialBlock>> entryCell : RutileMaterialBlocks.MATERIAL_BLOCKS.cellSet()) {
                FlagKey<?> flagKey = entryCell.getRowKey();
                var material = entryCell.getColumnKey();
                var blockEntry = entryCell.getValue();
                if (material != null) {
                    String materialName = material.getName();
                    if (material.hasFlag(flagKey) && material.getFlag(flagKey) instanceof BlockFlag blockFlag) {
                        List<String> tagPatterns = blockFlag.getTagPatterns();
                        for (String tagPattern : tagPatterns) {
                            List<TagLoader.EntryWithSource> tags = new ArrayList<>();
                            String tagName = tagPattern.formatted(materialName);
                            ResourceLocation tagId = ResourceLocation.tryParse(tagName);
                            tags.add(new TagLoader.EntryWithSource(
                                    TagEntry.element(BuiltInRegistries.BLOCK.getKey(blockEntry.get())),
                                    "Rutile Custom Tags"));
                            tagMap.computeIfAbsent(tagId, path -> new ArrayList<>()).addAll(tags);
                        }
                    }
                }
            }
        }
    }
}
