package dev.metallurgists.rutile.client;

import dev.metallurgists.rutile.api.RutileApi;
import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.material.flags.FlagKey;
import dev.metallurgists.rutile.api.material.flags.IMaterialFlag;
import dev.metallurgists.rutile.api.material.flags.IPartialHolder;
import dev.metallurgists.rutile.api.material.flags.ISpecialAssetGen;
import dev.metallurgists.rutile.api.runtime.assets.RutileDynamicResourcePack;
import dev.metallurgists.rutile.api.tag.TagPrefix;
import dev.metallurgists.rutile.util.ModelHelpers;

public class RutileModels {

    public static void registerMaterialAssets() {
        for (Material material : RutileApi.getMaterialRegistry().getAll()) {
            generatePartialModels(material);
            generateObjectAssets(material);
            generateSpecialAssets(material);
            generateFluidModels(material);
        }
    }

    private static void generateSpecialAssets(Material material) {
        for (FlagKey<? extends IMaterialFlag> flagKey : material.getFlags().getFlagKeys()) {
            var flag = material.getFlag(flagKey);
            if (flag instanceof ISpecialAssetGen specialAssetGen) {
                specialAssetGen.generateAssets(material);
            }
        }
    }

    private static void generatePartialModels(Material material) {
        for (FlagKey<? extends IMaterialFlag> flagKey : material.getFlags().getFlagKeys()) {
            var flag = material.getFlag(flagKey);
            if (flag instanceof IPartialHolder partialHolder) {
                RutileDynamicResourcePack.addPartialModel(partialHolder.getModelLocation(material), partialHolder.createModel(material));
            }
        }
    }

    private static void generateObjectAssets(Material material) {
        for (TagPrefix tagPrefix : RutileApi.getTagPrefixRegistry().getAll()) {
            if (tagPrefix.doGenerateItem(material)) {
                ModelHelpers.generatedItemModel(material, tagPrefix);
            }
            if (tagPrefix.doGenerateBlock(material)) {
                ModelHelpers.cubeAllBlockModel(material, tagPrefix);
            }
        }
    }

    public static void generateFluidModels(Material material) {
        //for (MaterialRegistryBuilder<Fluid> builder : flagContainer.getBuilders().values()) {
        //    MixinHelpers.addFluidTexture(material, builder.getFlagSource(), MaterialHelpers.getFluid(material, builder.getFlagSource()));
        //}
    }
}
