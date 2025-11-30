package dev.metallurgists.rutile.client;

import dev.metallurgists.rutile.api.dynamic_pack.asset.RutileDynamicResourcePack;
import dev.metallurgists.rutile.api.material.FlagRegistryTypes;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.builder.FlagContainer;
import dev.metallurgists.rutile.api.material.builder.MaterialRegistryBuilder;
import dev.metallurgists.rutile.api.material.flag.types.*;
import dev.metallurgists.rutile.api.registry.RutileAPI;
import dev.metallurgists.rutile.util.helpers.MaterialHelpers;
import dev.metallurgists.rutile.util.helpers.MixinHelpers;
import net.minecraft.world.level.material.Fluid;

public class RutileModels {

    public static void registerMaterialAssets() {
        for (Material material : RutileAPI.getMaterialRegistry().getAll()) {
            generatePartialModels(material);
            generateBuilderAssets(material);
            generateSpecialAssets(material);
            generateFluidModels(material);
        }
    }

    private static void generateSpecialAssets(Material material) {
        for (FlagContainer<?> flagContainer : material.getFlags().getFlagContainers()) {
            for (MaterialRegistryBuilder<?> builder : flagContainer.getBuilders().values()) {
                if (builder instanceof ISpecialAssetGen specialAssetGen) {
                    specialAssetGen.generateAssets(material);
                }
            }
        }
    }

    private static void generateBuilderAssets(Material material) {
        for (FlagContainer<?> flagContainer : material.getFlags().getFlagContainers()) {
            flagContainer.getBuilders().forEach((fs, b) -> b.registerAssets(material));
        }
    }

    private static void generatePartialModels(Material material) {
        for (FlagContainer<?> flagContainer : material.getFlags().getFlagContainers()) {
            for (MaterialRegistryBuilder<?> builder : flagContainer.getBuilders().values()) {
                if (builder instanceof IPartialHolder partialHolder) {
                    RutileDynamicResourcePack.addPartialModel(partialHolder.getModelLocation(material), partialHolder.createModel(material));
                }
            }
        }
    }

    public static void generateFluidModels(Material material) {
        FlagContainer<Fluid> flagContainer = material.getFlagContainer(FlagRegistryTypes.FLUID);
        if (flagContainer == null) return;
        for (MaterialRegistryBuilder<Fluid> builder : flagContainer.getBuilders().values()) {
            MixinHelpers.addFluidTexture(material, builder.getFlagSource(), MaterialHelpers.getFluid(material, builder.getFlagSource()));
        }
    }
}
