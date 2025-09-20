package dev.metallurgists.rutile.api.material.item;

import com.ibm.icu.impl.Pair;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.base.MaterialStack;
import dev.metallurgists.rutile.api.material.flag.FlagKey;
import dev.metallurgists.rutile.api.material.flag.types.IHaveTags;
import dev.metallurgists.rutile.util.ItemStackHashStrategy;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenCustomHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ItemMaterialData {

    public static final Map<Item, ItemMaterialInfo> ITEM_MATERIAL_INFO = new Object2ObjectOpenHashMap<>();

    public static final List<Pair<Supplier<? extends Item>, KeyedMaterial>> ITEM_MATERIAL_ENTRY = new ArrayList<>();
    public static final Map<Item, KeyedMaterial> ITEM_MATERIAL_ENTRY_COLLECTED = new Object2ObjectOpenHashMap<>();

    public static final Map<TagKey<Item>, KeyedMaterial> TAG_MATERIAL_ENTRY = new Object2ObjectLinkedOpenHashMap<>();
    public static final Map<Fluid, Material> FLUID_MATERIAL = new Object2ObjectOpenHashMap<>();

    public static final Map<KeyedMaterial, List<Supplier<? extends Item>>> MATERIAL_ENTRY_ITEM_MAP = new Object2ObjectOpenHashMap<>();
    public static final Map<KeyedMaterial, List<Supplier<? extends Block>>> MATERIAL_ENTRY_BLOCK_MAP = new Object2ObjectOpenHashMap<>();

    public static final Map<ItemStack, List<ItemStack>> UNRESOLVED_ITEM_MATERIAL_INFO = new Object2ObjectOpenCustomHashMap<>(
            ItemStackHashStrategy.comparingAllButCount());

    public static void registerMaterialInfo(ItemLike item, ItemMaterialInfo materialInfo) {
        ITEM_MATERIAL_INFO.put(item.asItem(), materialInfo);
    }

    public static ItemMaterialInfo getMaterialInfo(ItemLike item) {
        return ITEM_MATERIAL_INFO.get(item.asItem());
    }

    public static void clearMaterialInfo(ItemLike item) {
        ITEM_MATERIAL_INFO.remove(item.asItem());
    }

    public static void registerMaterialEntry(@NotNull Supplier<? extends ItemLike> supplier,
                                             @NotNull KeyedMaterial materialEntry) {
        registerItemEntry(supplier, materialEntry);
        ITEM_MATERIAL_ENTRY.add(Pair.of(() -> supplier.get().asItem(), materialEntry));
        var blockSupplier = convertToBlock(supplier);
        if (blockSupplier != null) {
            registerBlockEntry(blockSupplier, materialEntry);
        }
    }

    public static void registerMaterialEntries(@NotNull Collection<Supplier<? extends ItemLike>> items,
                                               @NotNull FlagKey<?> flagKey, @NotNull Material material) {
        if (!items.isEmpty()) {
            KeyedMaterial entry = new KeyedMaterial(flagKey, material);
            for (var supplier : items) {
                registerMaterialEntry(supplier, entry);
            }
        }
    }

    public static void registerMaterialEntry(@NotNull Supplier<? extends ItemLike> item,
                                             @NotNull FlagKey<?> flagKey, @NotNull Material material) {
        registerMaterialEntry(item, new KeyedMaterial(flagKey, material));
    }

    public static void registerMaterialEntry(@NotNull ItemLike item,
                                             @NotNull FlagKey<?> flagKey, @NotNull Material material) {
        registerMaterialEntry(() -> item, new KeyedMaterial(flagKey, material));
    }

    private static void registerItemEntry(@NotNull Supplier<? extends ItemLike> supplier,
                                          @NotNull KeyedMaterial keyedMaterial) {
        MATERIAL_ENTRY_ITEM_MAP.computeIfAbsent(keyedMaterial, k -> new ArrayList<>())
                .add(() -> supplier.get().asItem());
        if (!keyedMaterial.isEmpty()) {
            if (keyedMaterial.flagKey() instanceof IHaveTags tags && tags.getTagHolder().has(Registries.ITEM)) {
                for (TagKey<Item> tag : tags.getTagHolder().getTags(Registries.ITEM, keyedMaterial.material())) {
                    TAG_MATERIAL_ENTRY.putIfAbsent(tag, keyedMaterial);
                }
            }
        }
    }

    private static void registerBlockEntry(@NotNull Supplier<? extends Block> supplier,
                                           @NotNull KeyedMaterial keyedMaterial) {
        MATERIAL_ENTRY_BLOCK_MAP.computeIfAbsent(keyedMaterial, k -> new ArrayList<>())
                .add(supplier);
    }

    @SuppressWarnings("unchecked")
    public static @Nullable Supplier<? extends Block> convertToBlock(@NotNull Supplier<? extends ItemLike> supplier) {
        if (supplier instanceof DeferredHolder<?, ?> registryObject) {
            var key = registryObject.getKey();
            if (key.isFor(Registries.BLOCK)) {
                return (Supplier<? extends Block>) registryObject;
            }
        }
        return null;
    }

    public static void reinitializeMaterialData() {
        MATERIAL_ENTRY_ITEM_MAP.clear();
        MATERIAL_ENTRY_BLOCK_MAP.clear();
        ITEM_MATERIAL_ENTRY.clear();
        FLUID_MATERIAL.clear();
    }

    @ApiStatus.Internal
    public static void resolveItemMaterialInfos(RecipeOutput provider) {
        for (var iter = UNRESOLVED_ITEM_MATERIAL_INFO.entrySet().iterator(); iter.hasNext();) {
            var entry = iter.next();
            var stack = entry.getKey();
            var existingMaterialInfo = recurseFindMaterialInfo(ITEM_MATERIAL_INFO.get(stack.getItem()), stack);
            //if (existingMaterialInfo != null) {

            //}
            iter.remove();
        }
    }


    private static ItemMaterialInfo recurseFindMaterialInfo(ItemMaterialInfo info, ItemStack stack) {
        // grab material info from each input
        for (var input : UNRESOLVED_ITEM_MATERIAL_INFO.get(stack)) {
            // recurse if its nested inputs, not yet resolved
            if (UNRESOLVED_ITEM_MATERIAL_INFO.containsKey(input)) {
                info = recurseFindMaterialInfo(info, input);
            } else {
                // add the info from an item that is resolved (or not in the map to begin with)
                var singularMatInfo = getMaterialInfo(input.getItem());
                int inputCount = input.getCount();
                int outputCount = stack.getCount();
                if (singularMatInfo != null) { // if that material info exists
                    List<MaterialStack> stackList = new ArrayList<>();
                    for (var matStack : singularMatInfo.getMaterials()) {
                        stackList.add(matStack.multiply(inputCount).divide(outputCount));
                    }
                    if (info == null) { // if the info isn't set initialize it
                        info = new ItemMaterialInfo(stackList);
                        ITEM_MATERIAL_INFO.put(stack.getItem(), info);
                    } else { // otherwise, add to it
                        info.addMaterialStacks(stackList);
                    }
                }
            }
        }
        return info;
    }
}
