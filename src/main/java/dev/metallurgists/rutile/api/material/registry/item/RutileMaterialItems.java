package dev.metallurgists.rutile.api.material.registry.item;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.flag.FlagKey;
import dev.metallurgists.rutile.api.material.flag.types.IItemRegistry;
import dev.metallurgists.rutile.api.registrate.RutileRegistrate;
import dev.metallurgists.rutile.api.registry.RutileAPI;
import dev.metallurgists.rutile.api.registry.material.MaterialRegistry;

public class RutileMaterialItems {
    public static ImmutableTable.Builder<FlagKey<?>, Material, ItemEntry<? extends IMaterialItem>> MATERIAL_ITEMS_BUILDER = ImmutableTable.builder();

    public static Table<FlagKey<?>, Material, ItemEntry<? extends IMaterialItem>> MATERIAL_ITEMS;

    public static void generateMaterialItems() {
        for (var flagKey : RutileAPI.getRegisteredFlags().values()) {
            if (flagKey.constructDefault() instanceof IItemRegistry) {
                for (MaterialRegistry registry : RutileAPI.materialManager.getRegistries()) {
                    RutileRegistrate registrate = registry.getRegistrate();
                    for (Material material : registry.getAllMaterials()) {
                        var flag = material.getFlag(flagKey);
                        if (material.noRegister(flagKey)) continue;
                        if (flag instanceof IItemRegistry itemRegistry) {
                            registerMaterialItem(material, itemRegistry, flagKey, registrate);
                        }
                    }
                }
            }
        }
        MATERIAL_ITEMS = MATERIAL_ITEMS_BUILDER.build();
    }

    public static void registerMaterialItem(Material material, IItemRegistry itemRegistry, FlagKey<?> flagKey, RutileRegistrate registrate) {
        MATERIAL_ITEMS_BUILDER.put(flagKey, material, itemRegistry.registerItem(material, itemRegistry, registrate));
    }
}
