package dev.metallurgists.rutile.api.material.registry.item;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.flag.FlagKey;
import dev.metallurgists.rutile.api.material.flag.types.IItemRegistry;
import dev.metallurgists.rutile.api.plugin.IRutilePlugin;
import dev.metallurgists.rutile.api.plugin.RutilePluginFinder;
import dev.metallurgists.rutile.api.registry.RutileAPI;
import dev.metallurgists.rutile.api.registry.material.MaterialRegistry;
import dev.metallurgists.rutile.registry.RutileRegistries;

import java.util.Objects;

public class RutileMaterialItems {
    public static ImmutableTable.Builder<FlagKey<?>, Material, ItemEntry<? extends IMaterialItem>> MATERIAL_ITEMS_BUILDER = ImmutableTable.builder();

    public static Table<FlagKey<?>, Material, ItemEntry<? extends IMaterialItem>> MATERIAL_ITEMS;

    public static void generateMaterialItems() {
        for (var flagKey : RutileRegistries.FLAG_KEY_REGISTRY) {
            if (flagKey.constructDefault() instanceof IItemRegistry) {
                for (IRutilePlugin plugin : RutilePluginFinder.getModPlugins()) {
                    AbstractRegistrate<?> registrate = plugin.getRegistrate();
                    for (Material material : RutileRegistries.MATERIAL_REGISTRY) {
                        if (!Objects.equals(material.getNamespace(), registrate.getModid())) continue;
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

    public static void registerMaterialItem(Material material, IItemRegistry itemRegistry, FlagKey<?> flagKey, AbstractRegistrate<?> registrate) {
        MATERIAL_ITEMS_BUILDER.put(flagKey, material, itemRegistry.registerItem(material, itemRegistry, registrate));
    }
}
