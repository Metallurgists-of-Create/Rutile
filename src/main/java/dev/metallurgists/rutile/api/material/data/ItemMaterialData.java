package dev.metallurgists.rutile.api.material.data;

import com.mojang.datafixers.util.Pair;
import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.material.module.registry.RegistryModule;
import dev.metallurgists.rutile.api.material.stack.ItemMaterialInfo;
import dev.metallurgists.rutile.api.material.stack.MaterialStack;
import dev.metallurgists.rutile.api.registry.RutileRegistries;
import dev.metallurgists.rutile.registry.RutileModules;
import dev.metallurgists.rutile.util.ItemStackHashStrategy;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenCustomHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.Holder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ItemMaterialData {
    public static final Map<Item, ItemMaterialInfo> ITEM_MATERIAL_INFO = new Object2ObjectOpenHashMap<>();
    public static final List<Pair<Supplier<? extends Item>, MaterialEntry>> ITEM_MATERIAL_ENTRY = new ArrayList<>();
    public static final Map<Item, MaterialEntry> ITEM_MATERIAL_ENTRY_COLLECTED = new Object2ObjectOpenHashMap<>();

    public static final Map<TagKey<Item>, MaterialEntry> TAG_MATERIAL_ENTRY = new Object2ObjectLinkedOpenHashMap<>();

    public static final Map<MaterialEntry, List<Supplier<? extends Item>>> MATERIAL_ENTRY_ITEM_MAP = new Object2ObjectOpenHashMap<>();

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

    public static void registerMaterialEntry(@NotNull Supplier<? extends ItemLike> supplier, @NotNull MaterialEntry materialEntry) {
        registerItemEntry(supplier, materialEntry);
        ITEM_MATERIAL_ENTRY.add(Pair.of(() -> supplier.get().asItem(), materialEntry));
    }

    public static void registerMaterialEntries(@NotNull Collection<Supplier<? extends ItemLike>> items, @NotNull Holder<RegistryModule.Key> key, @NotNull Material material) {
        if (!items.isEmpty()) {
            MaterialEntry entry = new MaterialEntry(key, material);
            for (var supplier : items) {
                registerMaterialEntry(supplier, entry);
            }
        }
    }

    public static void registerMaterialEntry(@NotNull Supplier<? extends ItemLike> item, @NotNull Holder<RegistryModule.Key> key, @NotNull Material material) {
        registerMaterialEntry(item, new MaterialEntry(key, material));
    }

    public static void registerMaterialEntry(@NotNull ItemLike item, @NotNull Holder<RegistryModule.Key> key, @NotNull Material material) {
        registerMaterialEntry(() -> item, new MaterialEntry(key, material));
    }

    private static void registerItemEntry(@NotNull Supplier<? extends ItemLike> supplier, @NotNull MaterialEntry materialEntry) {
        MATERIAL_ENTRY_ITEM_MAP.computeIfAbsent(materialEntry, k -> new ArrayList<>())
                .add(() -> supplier.get().asItem());
        if (!materialEntry.isEmpty()) {
            //if (materialEntry.key() instanceof TaggableSource<?>) {
            //    for (TagKey<Item> tag : ((TaggableSource<Item>) materialEntry.source()).getAllTags(materialEntry.material())) {
            //        TAG_MATERIAL_ENTRY.putIfAbsent(tag, materialEntry);
            //    }
            //}
        }
    }

    public static void reinitializeMaterialData() {
        MATERIAL_ENTRY_ITEM_MAP.clear();
        ITEM_MATERIAL_ENTRY.clear();

        for (Material material : RutileRegistries.MATERIALS) {
            material.getModule(RutileModules.ITEM_REDIRECT).ifPresent(module -> {
                module.getRedirects().forEach((key, items) -> registerMaterialEntries(items, key, material));
            });
        }
    }

    @ApiStatus.Internal
    public static void resolveItemMaterialInfos(RecipeOutput provider) {
        for (var iter = UNRESOLVED_ITEM_MATERIAL_INFO.entrySet().iterator(); iter.hasNext();) {
            iter.remove();
        }
    }

    private static ItemMaterialInfo recurseFindMaterialInfo(ItemMaterialInfo info, ItemStack stack) {
        for (var input : UNRESOLVED_ITEM_MATERIAL_INFO.get(stack)) {
            if (UNRESOLVED_ITEM_MATERIAL_INFO.containsKey(input)) {
                info = recurseFindMaterialInfo(info, input);
            } else {
                var singularMatInfo = getMaterialInfo(input.getItem());
                int inputCount = input.getCount();
                int outputCount = stack.getCount();
                if (singularMatInfo != null) {
                    List<MaterialStack> stackList = new ArrayList<>();
                    for (var matStack : singularMatInfo.getMaterials()) {
                        stackList.add(matStack.multiply(inputCount).divide(outputCount));
                    }
                    if (info == null) {
                        info = new ItemMaterialInfo(stackList);
                        ITEM_MATERIAL_INFO.put(stack.getItem(), info);
                    } else {
                        info.addMaterialStacks(stackList);
                    }
                }
            }
        }
        return info;
    }
}
