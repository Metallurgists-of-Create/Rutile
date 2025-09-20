package dev.metallurgists.rutile.util.helpers;

import com.google.common.collect.Table;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.FluidEntry;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.flag.FlagKey;
import dev.metallurgists.rutile.api.material.flag.types.IHaveTags;
import dev.metallurgists.rutile.api.material.registry.block.IMaterialBlock;
import dev.metallurgists.rutile.api.material.registry.block.RutileMaterialBlocks;
import dev.metallurgists.rutile.api.material.registry.fluid.IMaterialFluid;
import dev.metallurgists.rutile.api.material.registry.item.RutileMaterialItems;
import dev.metallurgists.rutile.api.registrate.RutileClientFluidTypeExtensions;
import dev.metallurgists.rutile.mixin.BlockBehaviourAccessor;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.packs.VanillaBlockLoot;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.TagLoader;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
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

    public static <T> void generateDynamicTags(Map<ResourceLocation, List<TagLoader.EntryWithSource>> tagMap, Registry<T> registry) {
        if (registry == BuiltInRegistries.ITEM) {
            RutileMaterialItems.MATERIAL_ITEMS.rowMap().forEach((flag, map) -> {
                map.forEach((material, itemEntry) -> {
                    if (material != null && material.getFlag(flag) instanceof IHaveTags t && t.getTagHolder().has(Registries.ITEM)) {
                        if (itemEntry != null) {
                            List<TagKey<Item>> tags = t.getTagHolder().getTags(Registries.ITEM, material);
                            for (TagKey<Item> tag : tags) {
                                tagMap.computeIfAbsent(tag.location(), path -> new ArrayList<>()).add(makeItemEntry(itemEntry));
                            }
                        }
                    }
                });
            });
            RutileMaterialBlocks.MATERIAL_BLOCKS.rowMap().forEach((flag, map) -> {
                map.forEach((material, itemEntry) -> {
                    if (material != null && material.getFlag(flag) instanceof IHaveTags t && t.getTagHolder().has(Registries.ITEM)) {
                        if (itemEntry != null && itemEntry.asItem() != Items.AIR) {
                            List<TagKey<Item>> tags = t.getTagHolder().getTags(Registries.ITEM, material);
                            for (TagKey<Item> tag : tags) {
                                tagMap.computeIfAbsent(tag.location(), path -> new ArrayList<>()).add(makeItemEntry(itemEntry.asItem()));
                            }
                        }
                    }
                });
            });
        }
        if (registry == BuiltInRegistries.BLOCK) {
            for (Table.Cell<FlagKey<?>, Material, ? extends BlockEntry<? extends IMaterialBlock>> entryCell : RutileMaterialBlocks.MATERIAL_BLOCKS.cellSet()) {
                FlagKey<?> flagKey = entryCell.getRowKey();
                var material = entryCell.getColumnKey();
                var blockEntry = entryCell.getValue();
                if (material.getFlag(flagKey) instanceof IHaveTags t && t.getTagHolder().has(Registries.BLOCK)) {
                    if (blockEntry != null) {
                        List<TagKey<Block>> tags = t.getTagHolder().getTags(Registries.BLOCK, material);
                        for (TagKey<Block> tag : tags) {
                            tagMap.computeIfAbsent(tag.location(), path -> new ArrayList<>()).add(makeBlockEntry(blockEntry));
                        }
                    }
                }
            }
        }
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

        RutileMaterialBlocks.MATERIAL_BLOCKS.rowMap().forEach((flagKey, map) -> {
            MixinHelpers.addMaterialBlockLootTables(lootTables, flagKey, map, blockLoot, access);
        });
    }

    public static void addMaterialBlockLootTables(TriConsumer<ResourceLocation, LootTable, RegistryAccess.Frozen> lootTables,
                                                  FlagKey<?> flagKey,
                                                  Map<Material, BlockEntry<? extends IMaterialBlock>> map,
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

    public static void addFluidTexture(Material material, FlagKey<?> flagKey, Fluid value) {
        IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(value);
        if (extensions instanceof RutileClientFluidTypeExtensions rutileExtensions) {
            var flowTex = IMaterialFluid.getFlowingTexture(material, flagKey);
            var stillTex = IMaterialFluid.getSourceTexture(material, flagKey);
            rutileExtensions.setFlowingTexture(flowTex);
            rutileExtensions.setStillTexture(stillTex);
        }
    }
}
