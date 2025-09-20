package dev.metallurgists.rutile.api.material.registry.block;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.flag.FlagKey;
import dev.metallurgists.rutile.api.material.flag.types.IBlockRegistry;
import dev.metallurgists.rutile.api.plugin.IRutilePlugin;
import dev.metallurgists.rutile.api.plugin.RutilePluginFinder;
import dev.metallurgists.rutile.api.registry.RutileAPI;
import dev.metallurgists.rutile.api.registry.material.MaterialRegistry;
import dev.metallurgists.rutile.registry.RutileRegistries;

import java.util.Objects;

public class RutileMaterialBlocks {
    public static ImmutableTable.Builder<FlagKey<?>, Material, BlockEntry<? extends IMaterialBlock>> MATERIAL_BLOCKS_BUILDER = ImmutableTable.builder();

    public static Table<FlagKey<?>, Material, BlockEntry<? extends IMaterialBlock>> MATERIAL_BLOCKS;

    public static void generateMaterialBlocks() {
        for (var flagKey : RutileRegistries.FLAG_KEY_REGISTRY) {
            if (flagKey.constructDefault() instanceof IBlockRegistry) {
                for (IRutilePlugin plugin : RutilePluginFinder.getModPlugins()) {
                    AbstractRegistrate<?> registrate = plugin.getRegistrate();
                    for (Material material : RutileRegistries.MATERIAL_REGISTRY) {
                        if (!Objects.equals(material.getNamespace(), registrate.getModid())) continue;
                        var flag = material.getFlag(flagKey);
                        if (material.noRegister(flagKey)) continue;
                        if (flag instanceof IBlockRegistry blockRegistry) {
                            registerMaterialBlock(material, blockRegistry, flagKey, registrate);
                        }
                    }
                }
            }
        }
    }



    private static void registerMaterialBlock(Material material, IBlockRegistry blockRegistry, FlagKey<?> flagKey, AbstractRegistrate<?> registrate) {
        MATERIAL_BLOCKS_BUILDER.put(flagKey, material, blockRegistry.registerBlock(material, blockRegistry, registrate));
    }

    public static void buildMaterialBlockTable() {
        MATERIAL_BLOCKS = MATERIAL_BLOCKS_BUILDER.build();
    }
}
