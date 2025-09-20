package dev.metallurgists.rutile.client;

import dev.metallurgists.rutile.api.dynamic_pack.asset.RutileDynamicResourcePack;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.flag.FlagKey;
import dev.metallurgists.rutile.api.material.flag.types.*;
import dev.metallurgists.rutile.registry.RutileRegistries;
import dev.metallurgists.rutile.util.helpers.MaterialHelpers;
import dev.metallurgists.rutile.util.helpers.MixinHelpers;

public class RutileModels {

    public static void registerMaterialAssets() {
        for (Material material : RutileRegistries.MATERIAL_REGISTRY) {
            material.getFlags().getFlagKeys().forEach(flagKey -> {
                generateSpecialAssets(material, flagKey);
                generateItemModels(material, flagKey);
                generateBlockModels(material, flagKey);
                generatePartialModels(material, flagKey);
                generateFluidModels(material, flagKey);
            });
        }
    }

    private static void generateSpecialAssets(Material material, FlagKey<?> flagKey) {
        if (material.getFlag(flagKey) instanceof ISpecialAssetGen specialAssetGen) {
            specialAssetGen.generateAssets(material);
        }
    }

    private static void generateItemModels(Material material, FlagKey<?> flagKey) {
        if (material.getFlag(flagKey) instanceof IItemRegistry itemFlag) {
            itemFlag.registerItemAssets(material);
        }
    }

    private static void generateBlockModels(Material material, FlagKey<?> flagKey) {
        if (material.getFlag(flagKey) instanceof IBlockRegistry blockFlag) {
            blockFlag.registerBlockAssets(material);
        }
    }

    private static void generatePartialModels(Material material, FlagKey<?> flagKey) {
        var flag = material.getFlag(flagKey);
        if (flag instanceof IPartialHolder partialHolder) {
            RutileDynamicResourcePack.addPartialModel(partialHolder.getModelLocation(material), partialHolder.createModel(material));
        }
    }

    public static void generateFluidModels(Material material, FlagKey<?> flagKey) {
        var flag = material.getFlag(flagKey);
        if (flag instanceof IFluidRegistry) {
            MixinHelpers.addFluidTexture(material, flagKey, MaterialHelpers.getFluid(material, flagKey));
        }
    }

    public static String getFlagName(FlagKey<?> flagKey) {
        return flagKey.toString();
    }
}
