package dev.metallurgists.rutile.client;

import com.google.gson.JsonObject;
import dev.metallurgists.rutile.api.dynamic_pack.asset.RutileDynamicResourcePack;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.flag.FlagKey;
import dev.metallurgists.rutile.api.material.flag.types.IBlockRegistry;
import dev.metallurgists.rutile.api.material.flag.types.IItemRegistry;
import dev.metallurgists.rutile.api.material.flag.types.ISpecialAssetGen;
import dev.metallurgists.rutile.api.material.flag.types.ISpecialAssetLocation;
import dev.metallurgists.rutile.api.registry.RutileAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

public class RutileModels {

    public static void registerMaterialAssets() {
        for (Material material : RutileAPI.getRegisteredMaterials().values()) {
            material.getFlags().getFlagKeys().forEach(flagKey -> {
                generateSpecialAssets(material, flagKey);
                generateItemModels(material, flagKey);
                generateBlockModels(material, flagKey);
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

    public static String getFlagName(FlagKey<?> flagKey) {
        return flagKey.toString();
    }

    public static JsonObject simpleGeneratedModel(String parent, String texture) {
        JsonObject model = new JsonObject();
        model.addProperty("parent", parent);
        JsonObject textures = new JsonObject();
        textures.addProperty("layer0", texture);
        model.add("textures", textures);
        return model;
    }

    public static JsonObject simpleParentedModel(String parent) {
        JsonObject model = new JsonObject();
        model.addProperty("parent", parent);
        return model;
    }

    public static JsonObject simpleCubeAll(String texture) {
        JsonObject model = new JsonObject();
        model.addProperty("parent", "minecraft:block/cube_all");
        JsonObject textures = new JsonObject();
        textures.addProperty("all", texture);
        model.add("textures", textures);
        return model;
    }

    public static JsonObject simplePillar(String end, String side) {
        JsonObject model = new JsonObject();
        model.addProperty("parent", "minecraft:block/cube_column");
        JsonObject textures = new JsonObject();
        textures.addProperty("end", end);
        textures.addProperty("side", side);
        model.add("textures", textures);
        return model;
    }

    private static JsonObject singleVariantBlockstate(String model) {
        JsonObject blockstate = new JsonObject();
        JsonObject variants = new JsonObject();
        JsonObject variant = new JsonObject();
        variant.addProperty("model", model);
        variants.add("", variant);
        blockstate.add("variants", variants);
        return blockstate;
    }

    public static JsonObject simpleAxisBlockstate(String model) {
        JsonObject blockstate = new JsonObject();
        JsonObject variants = new JsonObject();
        JsonObject variantX = new JsonObject();
        JsonObject variantY = new JsonObject();
        JsonObject variantZ = new JsonObject();
        variantX.addProperty("model", model);
        variantX.addProperty("x", 90);
        variantX.addProperty("y", 90);
        variantY.addProperty("model", model);
        variantZ.addProperty("model", model);
        variantZ.addProperty("x", 90);
        variants.add("axis=x", variantX);
        variants.add("axis=y", variantY);
        variants.add("axis=z", variantZ);
        blockstate.add("variants", variants);
        return blockstate;
    }
}
