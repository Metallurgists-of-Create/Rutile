package dev.metallurgists.rutile.api.material.registry.fluid;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.tterrag.registrate.util.entry.FluidEntry;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.flag.FlagKey;
import dev.metallurgists.rutile.api.material.flag.types.IFluidRegistry;
import dev.metallurgists.rutile.api.registrate.RutileRegistrate;
import dev.metallurgists.rutile.api.registry.RutileAPI;
import dev.metallurgists.rutile.api.registry.material.MaterialRegistry;

public class RutileMaterialFluids {
    public static ImmutableTable.Builder<FlagKey<?>, Material, FluidEntry<? extends IMaterialFluid>> MATERIAL_FLUIDS_BUILDER = ImmutableTable.builder();

    public static Table<FlagKey<?>, Material, FluidEntry<? extends IMaterialFluid>> MATERIAL_FLUIDS;

    public static void generateMaterialFluids() {
        for (var flagKey : RutileAPI.getRegisteredFlags().values()) {
            if (flagKey.constructDefault() instanceof IFluidRegistry) {
                for (MaterialRegistry registry : RutileAPI.materialManager.getRegistries()) {
                    RutileRegistrate registrate = registry.getRegistrate();
                    for (Material material : registry.getAllMaterials()) {
                        var flag = material.getFlag(flagKey);
                        if (material.noRegister(flagKey)) continue;
                        if (flag instanceof IFluidRegistry fluidRegistry) {
                            registerMaterialFluid(material, fluidRegistry, flagKey, registrate);
                        }
                    }
                }
            }
        }
        MATERIAL_FLUIDS = MATERIAL_FLUIDS_BUILDER.build();
    }

    private static void registerMaterialFluid(Material material, IFluidRegistry fluidRegistry, FlagKey<?> flagKey, RutileRegistrate registrate) {
        MATERIAL_FLUIDS_BUILDER.put(flagKey, material, fluidRegistry.registerFluid(material, fluidRegistry, registrate));
    }
}
