package dev.metallurgists.rutile.api.material.registry.block;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.flag.FlagKey;
import dev.metallurgists.rutile.api.material.flag.types.IBlockRegistry;
import dev.metallurgists.rutile.api.registry.RutileAPI;
import dev.metallurgists.rutile.api.registry.material.MaterialRegistry;

public class RutileMaterialBlocks {
    public static ImmutableTable.Builder<FlagKey<?>, Material, BlockEntry<? extends IMaterialBlock>> MATERIAL_BLOCKS_BUILDER = ImmutableTable.builder();

    public static Table<FlagKey<?>, Material, BlockEntry<? extends IMaterialBlock>> MATERIAL_BLOCKS;

    public static void generateMaterialBlocks() {
        for (var flagKey : RutileAPI.getRegisteredFlags().values()) {
            if (flagKey.constructDefault() instanceof IBlockRegistry) {
                for (MaterialRegistry registry : RutileAPI.materialManager.getRegistries()) {
                    AbstractRegistrate<?> registrate = registry.getRegistrate();
                    for (Material material : registry.getAllMaterials()) {
                        var flag = material.getFlag(flagKey);
                        if (material.noRegister(flagKey)) continue;
                        if (flag instanceof IBlockRegistry blockRegistry) {
                            registerMaterialBlock(material, blockRegistry, flagKey, registrate);
                        }
                    }
                }
            }
        }
        MATERIAL_BLOCKS = MATERIAL_BLOCKS_BUILDER.build();
    }



    private static void registerMaterialBlock(Material material, IBlockRegistry blockRegistry, FlagKey<?> flagKey, AbstractRegistrate<?> registrate) {
        MATERIAL_BLOCKS_BUILDER.put(flagKey, material, blockRegistry.registerBlock(material, blockRegistry, registrate));
    }
}
