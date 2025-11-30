package dev.metallurgists.rutile.util.helpers;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.material.FlagRegistryTypes;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.base.MaterialLike;
import dev.metallurgists.rutile.api.material.builder.FlagContainer;
import dev.metallurgists.rutile.api.material.builder.FlagSource;
import dev.metallurgists.rutile.api.material.flag.types.*;
import dev.metallurgists.rutile.api.registry.RutileAPI;
import dev.metallurgists.rutile.registry.MaterialRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

import java.util.*;

public class MaterialHelpers {

    public static Table<FlagSource<Item>, Material, Item> getAllItems(boolean onlyBuilderRegistered) {
        ImmutableTable.Builder<FlagSource<Item>, Material, Item> tableBuilder = ImmutableTable.builder();
        for (var material : RutileAPI.getMaterialRegistry().getAll()) {
            var flagContainer = material.getFlagContainer(FlagRegistryTypes.ITEM);
            if (flagContainer == null) return tableBuilder.build();
            if (onlyBuilderRegistered) {
                for (var builder : flagContainer.getBuilders().values()) {
                    builder.setMaterialKey(material.getId());
                    tableBuilder.put(builder.getFlagSource(), material, BuiltInRegistries.ITEM.get(builder.getObjectId()));
                }
            } else {
                for (var entry : flagContainer.getObjects().entrySet()) {
                    var item = BuiltInRegistries.ITEM.get(entry.getValue());
                    tableBuilder.put(entry.getKey(), material, item);
                }
            }
        }
        return tableBuilder.build();
    }

    public static List<Item> getAllItems(MaterialLike material) {
        return getAllItems(material.asMaterial(), false);
    }

    public static List<Item> getAllItems(MaterialLike material, boolean onlyChemicalTooltippable) {
        List<Item> allItems = new ArrayList<>();
        for (var flag : material.asMaterial().getFlagContainer(FlagRegistryTypes.ITEM).getFlags()) {
            allItems.add(getItem(material, flag));
        }
        return allItems;
    }

    public static Table<FlagSource<Block>, Material, Block> getAllBlocks(boolean onlyBuilderRegistered) {
        ImmutableTable.Builder<FlagSource<Block>, Material, Block> tableBuilder = ImmutableTable.builder();
        for (var material : RutileAPI.getMaterialRegistry().getAll()) {
            var flagContainer = material.getFlagContainer(FlagRegistryTypes.BLOCK);
            if (flagContainer == null) return tableBuilder.build();
            if (onlyBuilderRegistered) {
                for (var builder : flagContainer.getBuilders().values()) {
                    builder.setMaterialKey(material.getId());
                    tableBuilder.put(builder.getFlagSource(), material, BuiltInRegistries.BLOCK.get(builder.getObjectId()));
                }
            } else {
                for (var entry : flagContainer.getObjects().entrySet()) {
                    var block = BuiltInRegistries.BLOCK.get(entry.getValue());
                    tableBuilder.put(entry.getKey(), material, block);
                }
            }
        }
        return tableBuilder.build();
    }

    public static List<Block> getAllBlocks(MaterialLike material) {
        return getAllBlocks(material, false);
    }

    public static List<Block> getAllBlocks(MaterialLike material, boolean onlyChemicalTooltippable) {
        List<Block> allBlocks = new ArrayList<>();
        for (var flag : material.asMaterial().getFlagContainer(FlagRegistryTypes.BLOCK).getFlags()) {
            allBlocks.add(getBlock(material, flag));
        }
        return allBlocks;
    }

    public static Table<FlagSource<Fluid>, Material, Fluid> getAllFluids(boolean onlyBuilderRegistered) {
        ImmutableTable.Builder<FlagSource<Fluid>, Material, Fluid> tableBuilder = ImmutableTable.builder();
        for (var material : RutileAPI.getMaterialRegistry().getAll()) {
            var flagContainer = material.getFlagContainer(FlagRegistryTypes.FLUID);
            if (flagContainer == null) return tableBuilder.build();
            if (onlyBuilderRegistered) {
                for (var builder : flagContainer.getBuilders().values()) {
                    builder.setMaterialKey(material.getId());
                    tableBuilder.put(builder.getFlagSource(), material, BuiltInRegistries.FLUID.get(builder.getObjectId()));
                }
            } else {
                for (var entry : flagContainer.getObjects().entrySet()) {
                    var fluid = BuiltInRegistries.FLUID.get(entry.getValue());
                    tableBuilder.put(entry.getKey(), material, fluid);
                }
            }
        }
        return tableBuilder.build();
    }

    public static List<Fluid> getAllFluids(MaterialLike material) {
        List<Fluid> allFluids = new ArrayList<>();
        for (var flag : material.asMaterial().getFlagContainer(FlagRegistryTypes.FLUID).getFlags()) {
            allFluids.add(getFluid(material, flag));
        }
        return allFluids;
    }

    public static Item getItem(MaterialLike material, FlagSource<Item> flagSource) {
        if (!material.asMaterial().hasFlag(flagSource)) throw new IllegalArgumentException("Material: " + material.asMaterial().getId() + " does not have the flag: " + flagSource.rlForm());
        return material.asMaterial().getFlagContainer(FlagRegistryTypes.ITEM).getObject(flagSource);
    }

    public static Block getBlock(MaterialLike material, FlagSource<Block> flagSource) {
        if (!material.asMaterial().hasFlag(flagSource)) throw new IllegalArgumentException("Material: " + material.asMaterial().getId() + " does not have the flag: " + flagSource.rlForm());
        return material.asMaterial().getFlagContainer(FlagRegistryTypes.BLOCK).getObject(flagSource);
    }

    public static Fluid getFluid(MaterialLike material, FlagSource<Fluid> flagSource) {
        FlagContainer<Fluid> flagContainer = material.asMaterial().getFlagContainer(FlagRegistryTypes.FLUID);
        ResourceLocation resultId = flagContainer.get(flagSource);
        return BuiltInRegistries.FLUID.get(resultId);
    }

    public static List<Item> getAllMaterialItems(MaterialLike material) {
        if (material.asMaterial() == null) {
            cannotGetAllItemsForNullMaterial();
            return new ArrayList<>();
        }
        List<Item> items = new ArrayList<>(MaterialHelpers.getAllItems(material));
        for (Block block : MaterialHelpers.getAllBlocks(material.asMaterial())) {
            items.add(block.asItem());
        }
        return items;
    }

    public static List<Item> getAllMaterialItemsForTooltips(MaterialLike material) {
        if (material.asMaterial() == null) {
            cannotGetAllItemsForNullMaterial();
            return new ArrayList<>();
        }
        List<Item> items = new ArrayList<>(MaterialHelpers.getAllItems(material, true));
        MaterialHelpers.getAllBlocks(material, true).forEach(block -> items.add(block.asItem()));
        return items;
    }

    private static void cannotGetAllItemsForNullMaterial() {
        Rutile.LOGGER.error("Material is null, cannot get all items for null material.");
    }

    public static boolean namespaceMatch(MaterialLike material, ResourceLocation location) {
        return material.asMaterial().getId().getNamespace().equals(location.getNamespace());
    }
}
