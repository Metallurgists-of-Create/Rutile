package dev.metallurgists.rutile.util;

import com.tterrag.registrate.util.entry.BlockEntry;
import dev.metallurgists.rutile.api.RutileApi;
import dev.metallurgists.rutile.api.material.ItemMaterialData;
import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.material.flags.FlagKey;
import dev.metallurgists.rutile.api.tag.TagPrefix;
import dev.metallurgists.rutile.mixin.BlockBehaviourAccessor;
import dev.metallurgists.rutile.registry.RutileMaterialBlocks;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.packs.VanillaBlockLoot;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.TagLoader;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import org.apache.commons.lang3.function.TriConsumer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class MixinHelpers {
    public static final ThreadLocal<HolderLookup.Provider> CURRENT_BE_SAVE_LOAD_REGISTRIES = new ThreadLocal<>();

    public static HolderLookup.Provider getCurrentBERegistries() {
        return CURRENT_BE_SAVE_LOAD_REGISTRIES.get();
    }

    public static <T> void generateDynamicTags(Map<ResourceLocation, List<TagLoader.EntryWithSource>> tagMap,
                                                 Registry<T> registry) {
        if (registry == BuiltInRegistries.ITEM) {
            ItemMaterialData.MATERIAL_ENTRY_ITEM_MAP.forEach((entry, itemLikes) -> {
                if (itemLikes.isEmpty()) return;
                var material = entry.material();
                var entries = itemLikes.stream()
                        .map(Supplier::get)
                        .map(MixinHelpers::makeItemEntry)
                        .collect(toArrayList());

                var prefixTagKeys = entry.tagPrefix().getAllItemTags(material);
                for (TagKey<Item> prefixTag : prefixTagKeys) {
                    tagMap.computeIfAbsent(prefixTag.location(), path -> new ArrayList<>()).addAll(entries);
                }
                for (TagKey<Item> materialTag : material.getItemTags()) {
                    tagMap.computeIfAbsent(materialTag.location(), path -> new ArrayList<>()).addAll(entries);
                }
            });
        } else if (registry == BuiltInRegistries.BLOCK) {
            ItemMaterialData.MATERIAL_ENTRY_BLOCK_MAP.forEach((entry, blocks) -> {
                if (blocks.isEmpty()) return;
                var material = entry.material();

                var entries = blocks.stream().map(MixinHelpers::makeBlockEntry).collect(toArrayList());
                var materialTags = entry.tagPrefix().getAllBlockTags(material);
                for (TagKey<Block> materialTag : materialTags) {
                    tagMap.computeIfAbsent(materialTag.location(), path -> new ArrayList<>()).addAll(entries);
                }

                // Add tool tags
                if (!entry.isIgnored() && !entry.tagPrefix().miningToolTag().isEmpty()) {
                    tagMap.computeIfAbsent(RutileApi.harvestLevels.getOrDefault(material.getBlockHarvestLevel(), BlockTags.INCORRECT_FOR_WOODEN_TOOL).location(),
                            path -> new ArrayList<>()).addAll(entries);
                    for (var tag : entry.tagPrefix().miningToolTag()) {
                        tagMap.computeIfAbsent(tag.location(), path -> new ArrayList<>()).addAll(entries);
                    }
                }
            });
        } //else
            //if (registry == BuiltInRegistries.FLUID) {
            //for (Material material : RutileApi.getMaterialRegistry().getAll()) {
            //    FluidFlag flag = material.getFlag(FlagKey.FLUID);
            //    if (flag == null) {
            //        continue;
            //    }
            //    for (FluidStorageKey key : FluidStorageKey.allKeys()) {
            //        Fluid fluid = flag.get(key);
            //        if (fluid == null) {
            //            continue;
            //        }
            //        ItemMaterialData.FLUID_MATERIAL.put(fluid, material);
//
            //        TagLoader.EntryWithSource entry = makeFluidEntry(fluid);
//
            //        ResourceLocation fluidIdTag = fluid.builtInRegistryHolder().key().location();
            //        fluidIdTag = ResourceLocation.fromNamespaceAndPath("c", fluidIdTag.getPath());
            //        tagMap.computeIfAbsent(fluidIdTag, path -> new ArrayList<>()).add(entry);
//
            //        FluidState state;
            //        if (fluid instanceof MaterialFluid materialFluid) {
            //            state = materialFluid.getState();
            //        } else {
            //            state = key.getDefaultFluidState();
            //        }
            //        if (state != null) {
            //            tagMap.computeIfAbsent(state.getTagKey().location(), path -> new ArrayList<>()).add(entry);
            //        }
//
            //        if (key.getExtraTag() != null) {
            //            tagMap.computeIfAbsent(key.getExtraTag().location(), path -> new ArrayList<>()).add(entry);
            //        }
            //    }
            //}
        //}
    }

    private static <T> Collector<T, ?, ArrayList<T>> toArrayList() {
        return Collectors.toCollection(ArrayList::new);
    }

    public static TagLoader.EntryWithSource makeItemEntry(ItemLike item) {
        return makeElementEntry(item.asItem().builtInRegistryHolder().key().location());
    }

    public static TagLoader.EntryWithSource makeBlockEntry(Supplier<? extends Block> block) {
        return makeBlockEntry(block.get());
    }

    public static TagLoader.EntryWithSource makeBlockEntry(Block block) {
        return makeElementEntry(block.builtInRegistryHolder().key().location());
    }

    public static TagLoader.EntryWithSource makeFluidEntry(Fluid fluid) {
        return makeElementEntry(fluid.builtInRegistryHolder().key().location());
    }

    public static TagLoader.EntryWithSource makeElementEntry(ResourceLocation id) {
        return new TagLoader.EntryWithSource(TagEntry.element(id), "Rutile Custom Tags");
    }

    public static TagLoader.EntryWithSource makeTagEntry(TagKey<?> tag) {
        return new TagLoader.EntryWithSource(TagEntry.tag(tag.location()), "Rutile Custom Tags");
    }

    public static void generateDynamicLoot(TriConsumer<ResourceLocation, LootTable, RegistryAccess.Frozen> lootTables,
                                           final RegistryAccess.Frozen access) {
        final VanillaBlockLoot blockLoot = new VanillaBlockLoot(access);

        Holder<Enchantment> fortune = access.registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(Enchantments.FORTUNE);

        RutileMaterialBlocks.MATERIAL_BLOCKS.rowMap().forEach((prefix, map) -> {
            MixinHelpers.addMaterialBlockLootTables(lootTables, prefix, map, blockLoot, access);
        });
    }

    public static void addMaterialBlockLootTables(TriConsumer<ResourceLocation, LootTable, RegistryAccess.Frozen> lootTables,
                                                  TagPrefix prefix,
                                                  Map<Material, ? extends BlockEntry<? extends Block>> map,
                                                  VanillaBlockLoot blockLoot, RegistryAccess.Frozen access) {
        map.forEach((material, blockEntry) -> {
            ResourceLocation lootTableId = blockEntry.getId().withPrefix("blocks/");
            ((BlockBehaviourAccessor) blockEntry.get())
                    .setDrops(ResourceKey.create(Registries.LOOT_TABLE, lootTableId));
            lootTables.accept(lootTableId,
                    blockLoot.createSingleItemTable(blockEntry.get()).setParamSet(LootContextParamSets.BLOCK).build(),
                    access);
        });
    }

    //public static void addFluidTexture(Material material, FluidStorage.FluidEntry value) {
    //    IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(value.getFluid().get());
    //    if (extensions instanceof GTClientFluidTypeExtensions gtExtensions && value.getBuilder() != null) {
    //        value.getBuilder().determineTextures(material, value.getKey());

    //        gtExtensions.setFlowingTexture(value.getBuilder().flowing());
    //        gtExtensions.setStillTexture(value.getBuilder().still());
    //    }
    //}
}
