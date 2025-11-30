package dev.metallurgists.rutile.util.helpers;

import dev.metallurgists.rutile.api.material.FlagRegistryTypes;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.builder.FlagSource;
import dev.metallurgists.rutile.api.material.registry.fluid.IMaterialFluid;
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
            MaterialHelpers.getAllItems(true).rowMap().forEach((flag, map) -> {
                map.forEach((material, item) -> {
                    if (material == null || item == null) return;
                    var builder = material.getFlags().getFlagContainer(FlagRegistryTypes.ITEM).getBuilders().get(flag);
                    if (builder != null) {
                        List<TagKey<Item>> tags = builder.getTagHolder().getTags(Registries.ITEM, material);
                        for (TagKey<Item> tag : tags) {
                            tagMap.computeIfAbsent(tag.location(), path -> new ArrayList<>()).add(makeItemEntry(item));
                        }
                    }
                });
            });
            MaterialHelpers.getAllBlocks(true).rowMap().forEach((flag, map) -> {
                map.forEach((material, block) -> {
                    if (material == null || block == null) return;
                    var builder = material.getFlags().getFlagContainer(FlagRegistryTypes.BLOCK).getBuilders().get(flag);
                    if (builder != null) {
                        List<TagKey<Item>> tags = builder.getTagHolder().getTags(Registries.ITEM, material);
                        for (TagKey<Item> tag : tags) {
                            tagMap.computeIfAbsent(tag.location(), path -> new ArrayList<>()).add(makeItemEntry(block.asItem()));
                        }
                    }
                });
            });
        }
        if (registry == BuiltInRegistries.BLOCK) {
            for (var cell : MaterialHelpers.getAllBlocks(true).cellSet()) {
                FlagSource<Block> flagSource = cell.getRowKey();
                var material = cell.getColumnKey(); var block = cell.getValue();
                if (material == null || block == null) return;
                var builder = material.getFlags().getFlagContainer(FlagRegistryTypes.BLOCK).getBuilders().get(flagSource);
                if (builder != null) {
                    List<TagKey<Block>> tags = builder.getTagHolder().getTags(Registries.BLOCK, material);
                    for (TagKey<Block> tag : tags) {
                        tagMap.computeIfAbsent(tag.location(), path -> new ArrayList<>()).add(makeBlockEntry(block));
                    }
                }
            }
        }
        if (registry == BuiltInRegistries.FLUID) {
            for (var cell : MaterialHelpers.getAllFluids(true).cellSet()) {
                FlagSource<Fluid> flagSource = cell.getRowKey();
                var material = cell.getColumnKey(); var fluid = cell.getValue();
                if (material == null || fluid == null) return;
                var builder = material.getFlags().getFlagContainer(FlagRegistryTypes.FLUID).getBuilders().get(flagSource);
                if (builder != null) {
                    List<TagKey<Fluid>> tags = builder.getTagHolder().getTags(Registries.FLUID, material);
                    for (TagKey<Fluid> tag : tags) {
                        tagMap.computeIfAbsent(tag.location(), path -> new ArrayList<>()).add(makeFluidEntry(fluid));
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

        MaterialHelpers.getAllBlocks(true).rowMap().forEach((flagKey, map) -> {
            MixinHelpers.addMaterialBlockLootTables(lootTables, map, blockLoot, access);
        });
    }

    public static void addMaterialBlockLootTables(TriConsumer<ResourceLocation, LootTable, RegistryAccess.Frozen> lootTables,
                                                  Map<Material, Block> map,
                                                  VanillaBlockLoot blockLoot, RegistryAccess.Frozen access) {
        map.forEach((material, block) -> {
            ResourceLocation lootTableId = BuiltInRegistries.BLOCK.getKey(block).withPrefix("blocks/");
            ((BlockBehaviourAccessor) block)
                    .setDrops(ResourceKey.create(Registries.LOOT_TABLE, lootTableId));
            lootTables.accept(lootTableId,
                    blockLoot.createSingleItemTable(block).setParamSet(LootContextParamSets.BLOCK).build(),
                    access);
        });
    }

    public static void addFluidTexture(Material material, FlagSource<Fluid> flagSource, Fluid value) {
        IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(value);
        if (extensions instanceof RutileClientFluidTypeExtensions rutileExtensions) {
            var flowTex = IMaterialFluid.getFlowingTexture(material, flagSource);
            var stillTex = IMaterialFluid.getSourceTexture(material, flagSource);
            rutileExtensions.setFlowingTexture(flowTex);
            rutileExtensions.setStillTexture(stillTex);
        }
    }
}
