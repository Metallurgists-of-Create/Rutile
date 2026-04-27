package dev.metallurgists.rutile.client;

import com.google.gson.JsonObject;
import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.fluid.MaterialFluid;
import dev.metallurgists.rutile.api.fluid.storage.FluidStorage;
import dev.metallurgists.rutile.api.fluid.storage.FluidStorageKey;
import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.material.flags.FlagKey;
import dev.metallurgists.rutile.api.material.flags.IMaterialFlag;
import dev.metallurgists.rutile.api.material.flags.IPartialHolder;
import dev.metallurgists.rutile.api.material.flags.ISpecialAssetGen;
import dev.metallurgists.rutile.api.registry.RutileRegistries;
import dev.metallurgists.rutile.api.runtime.assets.RutileDynamicResourcePack;
import dev.metallurgists.rutile.util.MixinHelpers;
import dev.metallurgists.rutile.util.ModelHelpers;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.material.Fluid;

import java.io.BufferedReader;
import java.io.IOException;

public class RutileModels {

    public static void registerMaterialAssets() {
        for (Material material : RutileRegistries.MATERIALS) {
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
        RutileRegistries.ITEM_SOURCES.forEach(src -> ModelHelpers.itemModel(material, src));
        RutileRegistries.BLOCK_SOURCES.forEach(src -> ModelHelpers.blockModel(material, src));
    }

    public static void generateFluidModels(Material material) {
        var fluidFlag = material.getFlag(FlagKey.FLUID);
        if (fluidFlag == null) return;

        for (FluidStorageKey key : FluidStorageKey.allKeys()) {
            FluidStorage storage = fluidFlag.getStorage();

            FluidStorage.FluidEntry fluidEntry = storage.getEntry(key);
            if (fluidEntry != null && fluidEntry.getBuilder() != null) {
                MixinHelpers.addFluidTexture(material, fluidEntry);
            }

            Fluid fluid = storage.get(key);
            if (fluid instanceof MaterialFluid materialFluid) {
                JsonObject original;
                try (BufferedReader reader = Minecraft.getInstance().getResourceManager()
                        .openAsReader(Rutile.id("models/item/bucket/bucket.json"))) {
                    original = GsonHelper.parse(reader, true);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                JsonObject newJson = original.deepCopy();
                newJson.addProperty("fluid", BuiltInRegistries.FLUID.getKey(materialFluid).toString());
                if (materialFluid.getFluidType().isLighterThanAir()) {
                    newJson.addProperty("flip_gas", true);
                }
                if (materialFluid.getFluidType().getLightLevel() > 0) {
                    newJson.addProperty("apply_fluid_luminosity", true);
                }

                RutileDynamicResourcePack.addItemModel(BuiltInRegistries.ITEM.getKey(materialFluid.getBucket()), newJson);
            }
        }
    }
}
