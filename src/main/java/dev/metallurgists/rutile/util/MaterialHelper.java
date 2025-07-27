package dev.metallurgists.rutile.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.flag.FlagKey;
import dev.metallurgists.rutile.api.material.flag.types.*;
import dev.metallurgists.rutile.api.material.registry.item.IMaterialItem;
import dev.metallurgists.rutile.api.registry.RutileAPI;
import dev.metallurgists.rutile.api.registry.RutileRegistry;
import dev.metallurgists.rutile.api.registry.material.MaterialRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

import java.util.*;

public class MaterialHelper {

    public static List<Item> getAllItems(Material material) {
        return getAllItems(material, false);
    }

    public static List<Item> getAllItems(Material material, boolean onlyChemicalTooltippable) {
        List<Item> allItems = new ArrayList<>();
        for (var flagKey : RutileAPI.getRegisteredFlags().values()) {
            if (!material.hasFlag(flagKey)) continue;
            var flag = material.getFlag(flagKey);
            if (flag instanceof IItemRegistry itemFlag) {
                if (onlyChemicalTooltippable)
                    if (itemFlag instanceof IConditionalComposition conditionalComposition && !conditionalComposition.shouldHaveComposition()) continue;
                allItems.add(getItem(material, flagKey));
            }
        }
        return allItems;
    }

    public static List<Block> getAllBlocks(Material material) {
        return getAllBlocks(material, false);
    }

    public static List<Block> getAllBlocks(Material material, boolean onlyChemicalTooltippable) {
        List<Block> allBlocks = new ArrayList<>();
        for (var flagKey : RutileAPI.getRegisteredFlags().values()) {
            if (!material.hasFlag(flagKey)) continue;
            var flag = material.getFlag(flagKey);
            if (flag instanceof IBlockRegistry blockFlag) {
                if (onlyChemicalTooltippable)
                    if (blockFlag instanceof IConditionalComposition conditionalComposition && !conditionalComposition.shouldHaveComposition()) continue;
                allBlocks.add(getBlock(material, blockFlag.getKey()));
            }
        }
        return allBlocks;
    }

    public static List<Fluid> getAllFluids(Material material) {
        List<Fluid> allFluids = new ArrayList<>();
        for (var flagKey : RutileAPI.getRegisteredFlags().values()) {
            if (!material.hasFlag(flagKey)) continue;
            var flag = material.getFlag(flagKey);
            if (flag instanceof IFluidRegistry fluidFlag) {
                allFluids.add(getFluid(material, fluidFlag.getKey()));
            }
        }
        return allFluids;
    }

    public static Item getItem(Material material, FlagKey<?> flagKey) {
        if (!material.hasFlag(flagKey)) throw new IllegalArgumentException("Material: " + material.getId() + " does not have the flag: " + flagKey.toString());
        if (!(material.getFlag(flagKey) instanceof IItemRegistry flag)) throw new IllegalArgumentException("Flag: " + flagKey.toString() + " is not an item flag");
        ResourceLocation resultId = flag.getExistingId(material);
        Item item = BuiltInRegistries.ITEM.get(resultId);
        return item;
    }

    public static Block getBlock(Material material, FlagKey<?> flagKey) {
        if (!material.hasFlag(flagKey)) throw new IllegalArgumentException("Material: " + material.getId() + " does not have the flag: " + flagKey.toString());
        if (!(material.getFlag(flagKey) instanceof IBlockRegistry flag)) throw new IllegalArgumentException("Flag: " + flagKey.toString() + " is not a block flag");
        ResourceLocation resultId = flag.getExistingId(material);
        Block block = BuiltInRegistries.BLOCK.get(resultId);
        return block;
    }

    public static Fluid getFluid(Material material, FlagKey<?> flagKey) {
        if (!material.hasFlag(flagKey)) throw new IllegalArgumentException("Material: " + material.getId() + " does not have the flag: " + flagKey.toString());
        if (!(material.getFlag(flagKey) instanceof IFluidRegistry flag)) throw new IllegalArgumentException("Flag: " + flagKey.toString() + " is not a fluid flag");
        ResourceLocation resultId = flag.getExistingId(material);
        Fluid fluid = BuiltInRegistries.FLUID.get(resultId);
        return fluid;
    }

    public static List<Item> getAllMaterialItems(Material material) {
        if (material == null) {
            cannotGetAllItemsForNullMaterial();
            return new ArrayList<>();
        }
        List<Item> items = new ArrayList<>(MaterialHelper.getAllItems(material));
        for (Block block : MaterialHelper.getAllBlocks(material)) {
            items.add(block.asItem());
        }
        return items;
    }

    public static List<Item> getAllMaterialItemsForTooltips(Material material) {
        if (material == null) {
            cannotGetAllItemsForNullMaterial();
            return new ArrayList<>();
        }
        List<Item> items = new ArrayList<>(MaterialHelper.getAllItems(material, true));
        MaterialHelper.getAllBlocks(material, true).forEach(block -> items.add(block.asItem()));
        return items;
    }

    private static void cannotGetAllItemsForNullMaterial() {
        Rutile.LOGGER.error("Material is null, cannot get all items for null material.");
    }

    public static String getNameForRecipe(Material material, FlagKey<?> flagKey) {
        if (material.getFlag(flagKey) instanceof IIdPattern idPattern) {
            String namespacePrefix = Objects.equals(material.getNamespace(), "rutile") ? "" : material.getNamespace() + "_";
            return namespacePrefix + idPattern.getIdPattern().formatted(material.getName());
        } else throw new IllegalArgumentException("FlagKey: " + flagKey.toString() + " does not implement IIdPattern and thus cannot be used for a recipe path.");
    }

    public static boolean hasExternalId(Material material, FlagKey<?> flagKey) {
        return material.noRegister(flagKey);
    }
}
