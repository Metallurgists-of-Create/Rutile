package dev.metallurgists.rutile.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.data.client.manager.MaterialAssetManager;
import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.material.flags.FlagKey;
import dev.metallurgists.rutile.api.material.registry.asset.MaterialAsset;
import dev.metallurgists.rutile.api.runtime.assets.RutileDynamicResourcePack;
import dev.metallurgists.rutile.api.tag.TagPrefix;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

import java.util.List;
import java.util.function.BiConsumer;

public class ModelHelpers {

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

    public static JsonObject singleVariantBlockstate(String model) {
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

    //These will never overlap. I know this because they're mine
    public static List<String> defaultFlagSources = List.of("rutile", "metallurgica");

    public static String getFlagName(TagPrefix tagPrefix) {
        return (defaultFlagSources.contains(tagPrefix.getModId()) ? "" : tagPrefix.getModId() + "_") + tagPrefix.getName();
    }

    public static boolean isTexturePresent(Material material, TagPrefix tagPrefix, String type) {
        return isTexturePresent(material, tagPrefix, "", type);
    }

    public static boolean isTexturePresent(Material material, TagPrefix tagPrefix, String suffix, String type) {
        ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
        String flag = getFlagName(tagPrefix);
        var texture = resourceManager.getResource(ResourceLocation.fromNamespaceAndPath(material.getModId(), "textures/"+type+"/materials/" + material.getName() + "/" + flag + suffix + ".png"));
        return texture.isPresent();
    }

    public static String textureOrNull(String pathFormat, Material material, boolean texturePresent) {
        ResourceLocation materialLocation = texturePresent ? material.getId() : Rutile.id("null");
        return pathFormat.formatted(materialLocation.getNamespace(), materialLocation.getPath());
    }

    public static void itemModel(Material material, TagPrefix tagPrefix) {
        MaterialAsset materialAsset = MaterialAssetManager.getInstance().getAsset(tagPrefix, material);
        ResourceLocation path = ResourceLocation.fromNamespaceAndPath(material.getModId(), tagPrefix.idPattern().formatted(material.getName()));
        if (materialAsset != null) {
            toPack(RutileDynamicResourcePack::addItemModel, path, materialAsset.createJson());
        } else ModelHelpers.generatedItemModel(material, tagPrefix, path);
    }

    public static void generatedItemModel(Material material, TagPrefix tagPrefix, ResourceLocation path) {
        String flagName = getFlagName(tagPrefix);
        boolean texturePresent = isTexturePresent(material, tagPrefix, "item");
        String texture = textureOrNull("%s:item/materials/%s/"+flagName, material, texturePresent);
        toPack(RutileDynamicResourcePack::addItemModel, path, ModelHelpers.simpleGeneratedModel("minecraft:item/generated", texture));
    }

    public static void blockModel(Material material, TagPrefix tagPrefix) {
        MaterialAsset materialAsset = MaterialAssetManager.getInstance().getAsset(tagPrefix, material);
        ResourceLocation path = ResourceLocation.fromNamespaceAndPath(material.getModId(), tagPrefix.idPattern().formatted(material.getName()));
        if (materialAsset != null) {
            toPack(RutileDynamicResourcePack::addBlockModel, path, materialAsset.createJson());
            toPack(RutileDynamicResourcePack::addBlockState, path, ModelHelpers.singleVariantBlockstate(material.getModId() + ":block/" + tagPrefix.idPattern().formatted(material.getName())));
            toPack(RutileDynamicResourcePack::addItemModel, path, ModelHelpers.simpleParentedModel(material.getModId() + ":block/" + tagPrefix.idPattern().formatted(material.getName())));
        } else ModelHelpers.blockModel(material, tagPrefix, path);
    }

    public static void blockModel(Material material, TagPrefix tagPrefix, ResourceLocation path) {
        TagPrefix.BlockAssetProperties assetProps = tagPrefix.blockAssetProperties();
        if (assetProps != null) {
            if (assetProps.hasModel()) {
                toPack(RutileDynamicResourcePack::addBlockModel, path, assetProps.model().apply(material, tagPrefix));
            } else toPack(RutileDynamicResourcePack::addBlockModel, path, BlockModel.cube(material, tagPrefix));
            if (assetProps.hasBlockState()) {
                toPack(RutileDynamicResourcePack::addBlockState, path, assetProps.blockState().apply(material, tagPrefix));
            } else toPack(RutileDynamicResourcePack::addBlockState, path, BlockState.singleVariant(material, tagPrefix));
            if (assetProps.hasItemModel()) {
                toPack(RutileDynamicResourcePack::addItemModel, path, assetProps.itemModel().apply(material, tagPrefix));
            } else toPack(RutileDynamicResourcePack::addItemModel, path, ItemModel.blockParent(material, tagPrefix));
        } else {
            cubeAllBlockModel(material, tagPrefix, path);
        }
    }

    public static void cubeAllBlockModel(Material material, TagPrefix tagPrefix, ResourceLocation path) {
        String flagName = getFlagName(tagPrefix);
        boolean texturePresent = isTexturePresent(material, tagPrefix, "block");
        String texture = textureOrNull("%s:block/materials/%s/"+flagName, material, texturePresent);
        toPack(RutileDynamicResourcePack::addBlockModel, path, ModelHelpers.simpleCubeAll(texture));
        toPack(RutileDynamicResourcePack::addBlockState, path, ModelHelpers.singleVariantBlockstate(material.getModId() + ":block/" + tagPrefix.idPattern().formatted(material.getName())));
        toPack(RutileDynamicResourcePack::addItemModel, path, ModelHelpers.simpleParentedModel(material.getModId() + ":block/" + tagPrefix.idPattern().formatted(material.getName())));
    }

    public static class BlockModel {

        public static JsonElement cube(Material material, TagPrefix tagPrefix) {
            String flagName = getFlagName(tagPrefix);
            boolean texturePresent = isTexturePresent(material, tagPrefix, "block");
            String texture = textureOrNull("%s:block/materials/%s/"+flagName, material, texturePresent);
            return ModelHelpers.simpleCubeAll(texture);
        }

        public static JsonElement pillar(Material material, TagPrefix tagPrefix) {
            String flagName = getFlagName(tagPrefix);
            boolean endPresent = isTexturePresent(material, tagPrefix, "_end", "block");
            boolean sidePresent = isTexturePresent(material, tagPrefix, "_side", "block");
            String endTexture = textureOrNull("%s:block/materials/%s/"+flagName+"_end", material, endPresent);
            String sideTexture = textureOrNull("%s:block/materials/%s/"+flagName+"_side", material, sidePresent);
            return ModelHelpers.simplePillar(endTexture, sideTexture);
        }
    }

    public static class BlockState {

        public static JsonElement singleVariant(Material material, TagPrefix tagPrefix) {
            return ModelHelpers.singleVariantBlockstate(material.getModId() + ":block/" + tagPrefix.idPattern().formatted(material.getName()));
        }

        public static JsonElement axis(Material material, TagPrefix tagPrefix) {
            return ModelHelpers.simpleAxisBlockstate(material.getModId() + ":block/" + tagPrefix.idPattern().formatted(material.getName()));
        }
    }

    public static class ItemModel {

        public static JsonElement blockParent(Material material, TagPrefix tagPrefix) {
            return ModelHelpers.simpleParentedModel(material.getModId() + ":block/" + tagPrefix.idPattern().formatted(material.getName()));
        }
    }

    public static void toPack(BiConsumer<ResourceLocation, JsonElement> adder, ResourceLocation assetLocation, JsonElement assetJson) {
        adder.accept(assetLocation, assetJson);
    }
}
