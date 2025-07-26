package dev.metallurgists.rutile.api.material.registry.block;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.flag.FlagKey;
import dev.metallurgists.rutile.api.material.flag.types.IBlockRegistry;
import dev.metallurgists.rutile.api.registrate.RutileRegistrate;
import dev.metallurgists.rutile.api.registry.RutileAPI;

import java.util.Arrays;

public class RutileMaterialBlocks {
    public static ImmutableTable.Builder<FlagKey<?>, Material, BlockEntry<? extends IMaterialBlock>> MATERIAL_BLOCKS_BUILDER = ImmutableTable.builder();

    public static Table<FlagKey<?>, Material, BlockEntry<? extends IMaterialBlock>> MATERIAL_BLOCKS;

    public static void generateMaterialBlocks(RutileRegistrate registrate, String... namespaces) {
        for (Material material : RutileAPI.getRegisteredMaterials().values()) {
            if (!Arrays.stream(namespaces).toList().contains(material.getNamespace())) continue;
            for (FlagKey<?> flagKey : material.getFlags().getFlagKeys()) {
                var flag = material.getFlag(flagKey);
                if (!material.noRegister(flagKey)) {
                    if (flag instanceof IBlockRegistry blockRegistry) {
                        registerMaterialBlock(blockRegistry, material, flagKey, registrate);
                    }
                }
            }
        }
    }


    private static void registerMaterialBlock(IBlockRegistry blockRegistry, Material material, FlagKey<?> flagKey, RutileRegistrate registrate) {
        MATERIAL_BLOCKS_BUILDER.put(flagKey, material, blockRegistry.registerBlock(material, blockRegistry, registrate));
    }
}
