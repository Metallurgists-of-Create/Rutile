package dev.metallurgists.rutile.util.helpers;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.material.base.MaterialLike;
import dev.metallurgists.rutile.api.material.flag.FlagKey;
import dev.metallurgists.rutile.api.material.flag.types.*;
import dev.metallurgists.rutile.api.registry.RutileAPI;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

import java.util.*;

public class MaterialHelpers {

    public static List<Item> getAllItems(MaterialLike material) {
        return getAllItems(material.asMaterial(), false);
    }

    public static List<Item> getAllItems(MaterialLike material, boolean onlyChemicalTooltippable) {
        List<Item> allItems = new ArrayList<>();
        for (var flagKey : RutileAPI.getRegisteredFlags().values()) {
            if (!material.asMaterial().hasFlag(flagKey)) continue;
            var flag = material.asMaterial().getFlag(flagKey);
            if (flag instanceof IItemRegistry itemFlag) {
                if (onlyChemicalTooltippable)
                    if (itemFlag instanceof IConditionalComposition conditionalComposition && !conditionalComposition.shouldHaveComposition()) continue;
                allItems.add(getItem(material, flagKey));
            }
        }
        return allItems;
    }

    public static List<Block> getAllBlocks(MaterialLike material) {
        return getAllBlocks(material, false);
    }

    public static List<Block> getAllBlocks(MaterialLike material, boolean onlyChemicalTooltippable) {
        List<Block> allBlocks = new ArrayList<>();
        for (var flagKey : RutileAPI.getRegisteredFlags().values()) {
            if (!material.asMaterial().hasFlag(flagKey)) continue;
            var flag = material.asMaterial().getFlag(flagKey);
            if (flag instanceof IBlockRegistry blockFlag) {
                if (onlyChemicalTooltippable)
                    if (blockFlag instanceof IConditionalComposition conditionalComposition && !conditionalComposition.shouldHaveComposition()) continue;
                allBlocks.add(getBlock(material, blockFlag.getKey()));
            }
        }
        return allBlocks;
    }

    public static List<Fluid> getAllFluids(MaterialLike material) {
        List<Fluid> allFluids = new ArrayList<>();
        for (var flagKey : RutileAPI.getRegisteredFlags().values()) {
            if (!material.asMaterial().hasFlag(flagKey)) continue;
            var flag = material.asMaterial().getFlag(flagKey);
            if (flag instanceof IFluidRegistry fluidFlag) {
                allFluids.add(getFluid(material, fluidFlag.getKey()));
            }
        }
        return allFluids;
    }

    public static Item getItem(MaterialLike material, FlagKey<?> flagKey) {
        if (!material.asMaterial().hasFlag(flagKey)) throw new IllegalArgumentException("Material: " + material.asMaterial().getId() + " does not have the flag: " + flagKey.toString());
        if (!(material.asMaterial().getFlag(flagKey) instanceof IItemRegistry flag)) throw new IllegalArgumentException("Flag: " + flagKey.toString() + " is not an item flag");
        ResourceLocation resultId = flag.getExistingId(material.asMaterial());
        Item item = BuiltInRegistries.ITEM.get(resultId);
        return item;
    }

    public static Block getBlock(MaterialLike material, FlagKey<?> flagKey) {
        if (!material.asMaterial().hasFlag(flagKey)) throw new IllegalArgumentException("Material: " + material.asMaterial().getId() + " does not have the flag: " + flagKey.toString());
        if (!(material.asMaterial().getFlag(flagKey) instanceof IBlockRegistry flag)) throw new IllegalArgumentException("Flag: " + flagKey.toString() + " is not a block flag");
        ResourceLocation resultId = flag.getExistingId(material.asMaterial());
        Block block = BuiltInRegistries.BLOCK.get(resultId);
        return block;
    }

    public static Fluid getFluid(MaterialLike material, FlagKey<?> flagKey) {
        if (!material.asMaterial().hasFlag(flagKey)) throw new IllegalArgumentException("Material: " + material.asMaterial().getId() + " does not have the flag: " + flagKey.toString());
        if (!(material.asMaterial().getFlag(flagKey) instanceof IFluidRegistry flag)) throw new IllegalArgumentException("Flag: " + flagKey.toString() + " is not a fluid flag");
        ResourceLocation resultId = flag.getExistingId(material.asMaterial());
        Fluid fluid = BuiltInRegistries.FLUID.get(resultId);
        return fluid;
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

    public static String getNameForRecipe(MaterialLike material, FlagKey<?> flagKey) {
        if (material.asMaterial().getFlag(flagKey) instanceof IIdPattern idPattern) {
            String namespacePrefix = Objects.equals(material.asMaterial().getNamespace(), "rutile") ? "" : material.asMaterial().getNamespace() + "_";
            return namespacePrefix + idPattern.getIdPattern().formatted(material.asMaterial().getName());
        } else throw new IllegalArgumentException("FlagKey: " + flagKey.toString() + " does not implement IIdPattern and thus cannot be used for a recipe path.");
    }

    public static boolean hasExternalId(MaterialLike material, FlagKey<?> flagKey) {
        return material.asMaterial().noRegister(flagKey);
    }
}
