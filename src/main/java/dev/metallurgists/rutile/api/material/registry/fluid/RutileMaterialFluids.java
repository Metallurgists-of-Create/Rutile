package dev.metallurgists.rutile.api.material.registry.fluid;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.tterrag.registrate.util.entry.FluidEntry;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.flag.FlagKey;
import dev.metallurgists.rutile.api.material.flag.types.IFluidRegistry;
import dev.metallurgists.rutile.api.registrate.RutileRegistrate;
import dev.metallurgists.rutile.api.registry.RutileAPI;

import java.util.Arrays;

public class RutileMaterialFluids {
    public static ImmutableTable.Builder<FlagKey<?>, Material, FluidEntry<? extends IMaterialFluid>> MATERIAL_FLUIDS_BUILDER = ImmutableTable.builder();

    public static Table<FlagKey<?>, Material, FluidEntry<? extends IMaterialFluid>> MATERIAL_FLUIDS;

    public static void generateMaterialFluids(RutileRegistrate registrate, String... namespaces) {
        for (Material material : RutileAPI.getRegisteredMaterials().values()) {
            if (!Arrays.stream(namespaces).toList().contains(material.getNamespace())) continue;
            for (FlagKey<?> flagKey : material.getFlags().getFlagKeys()) {
                var flag = material.getFlag(flagKey);
                if (!material.noRegister(flagKey)) {
                    if (flag instanceof IFluidRegistry fluidRegistry) {
                        registerMaterialFluid(fluidRegistry, material, flagKey, registrate);
                    }
                }
            }
        }
    }

    private static void registerMaterialFluid(IFluidRegistry fluidRegistry, Material material, FlagKey<?> flagKey, RutileRegistrate registrate) {
        MATERIAL_FLUIDS_BUILDER.put(flagKey, material, fluidRegistry.registerFluid(material, fluidRegistry, registrate));
    }
}
