package dev.metallurgists.rutile.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.data.client.manager.MaterialAssetManager;
import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.material.module.registry.RegistryModule;
import dev.metallurgists.rutile.api.material.registry.asset.MaterialAsset;
import dev.metallurgists.rutile.api.runtime.assets.RutileDynamicResourcePack;
import dev.metallurgists.rutile.registry.RutileModules;
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

    public static String getResourceName(RegistryModule.Key registerKey) {
        return (defaultFlagSources.contains(registerKey.loc().getNamespace()) ? "" : registerKey.loc().getNamespace() + "_") + registerKey.loc().getPath().replace('/', '_');
    }

    public static boolean isTexturePresent(Material material, RegistryModule.Key registerKey, String type) {
        return isTexturePresent(material, registerKey, "", type);
    }

    public static boolean isTexturePresent(Material material, RegistryModule.Key registerKey, String suffix, String type) {
        ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
        String flag = getResourceName(registerKey);
        var texture = resourceManager.getResource(ResourceLocation.fromNamespaceAndPath(material.getModId(), "textures/"+type+"/materials/" + material.getName() + "/" + flag + suffix + ".png"));
        return texture.isPresent();
    }

    public static String textureOrNull(String pathFormat, Material material, boolean texturePresent) {
        ResourceLocation materialLocation = texturePresent ? material.getId() : Rutile.id("null");
        return pathFormat.formatted(materialLocation.getNamespace(), materialLocation.getPath());
    }

    public static void itemModel(Material material, RegistryModule.Key registerKey) {
        MaterialAsset materialAsset = MaterialAssetManager.getInstance().getAsset(registerKey, material);
        ResourceLocation path = ResourceLocation.fromNamespaceAndPath(material.getModId(), registerKey.idPattern().formatted(registerKey.getMaterialName(material)));
        if (materialAsset != null) {
            toPack(RutileDynamicResourcePack::addItemModel, path, materialAsset.createJson());
        } else ModelHelpers.generatedItemModel(material, registerKey, path);
    }

    public static void generatedItemModel(Material material, RegistryModule.Key registerKey, ResourceLocation path) {
        String flagName = getResourceName(registerKey);
        boolean texturePresent = isTexturePresent(material, registerKey, "item");
        String texture = textureOrNull("%s:item/materials/%s/"+flagName, material, texturePresent);
        toPack(RutileDynamicResourcePack::addItemModel, path, ModelHelpers.simpleGeneratedModel("minecraft:item/generated", texture));
    }

    public static void blockModel(Material material, RegistryModule.Key registerKey) {
        MaterialAsset materialAsset = MaterialAssetManager.getInstance().getAsset(registerKey, material);
        ResourceLocation path = ResourceLocation.fromNamespaceAndPath(material.getModId(), registerKey.idPattern().formatted(registerKey.getMaterialName(material)));
        if (materialAsset != null) {
            toPack(RutileDynamicResourcePack::addBlockModel, path, materialAsset.createJson());
            toPack(RutileDynamicResourcePack::addBlockState, path, ModelHelpers.singleVariantBlockstate(material.getModId() + ":block/" + registerKey.idPattern().formatted(registerKey.getMaterialName(material))));
            toPack(RutileDynamicResourcePack::addItemModel, path, ModelHelpers.simpleParentedModel(material.getModId() + ":block/" + registerKey.idPattern().formatted(registerKey.getMaterialName(material))));
        } else ModelHelpers.blockModel(material, registerKey, path);
    }

    public static void blockModel(Material material, RegistryModule.Key registerKey, ResourceLocation path) {
        var blockAssets = material.getModule(RutileModules.BLOCK_ASSETS);
        if (blockAssets.isPresent()) {
            var properties = blockAssets.get().getProperties(registerKey);
            if (properties.isPresent()) {
                if (properties.get().hasModel()) {
                    toPack(RutileDynamicResourcePack::addBlockModel, path, properties.get().model().apply(material, registerKey));
                } else toPack(RutileDynamicResourcePack::addBlockModel, path, BlockModel.cube(material, registerKey));
                if (properties.get().hasBlockState()) {
                    toPack(RutileDynamicResourcePack::addBlockState, path, properties.get().blockState().apply(material, registerKey));
                } else toPack(RutileDynamicResourcePack::addBlockState, path, BlockState.singleVariant(material, registerKey));
                if (properties.get().hasItemModel()) {
                    toPack(RutileDynamicResourcePack::addItemModel, path, properties.get().itemModel().apply(material, registerKey));
                } else toPack(RutileDynamicResourcePack::addItemModel, path, ItemModel.blockParent(material, registerKey));
            }
        } else {
            cubeAllBlockModel(material, registerKey, path);
        }
    }

    public static void cubeAllBlockModel(Material material, RegistryModule.Key registerKey, ResourceLocation path) {
        String flagName = getResourceName(registerKey);
        boolean texturePresent = isTexturePresent(material, registerKey, "block");
        String texture = textureOrNull("%s:block/materials/%s/"+flagName, material, texturePresent);
        toPack(RutileDynamicResourcePack::addBlockModel, path, ModelHelpers.simpleCubeAll(texture));
        toPack(RutileDynamicResourcePack::addBlockState, path, ModelHelpers.singleVariantBlockstate(material.getModId() + ":block/" + registerKey.idPattern().formatted(registerKey.getMaterialName(material))));
        toPack(RutileDynamicResourcePack::addItemModel, path, ModelHelpers.simpleParentedModel(material.getModId() + ":block/" + registerKey.idPattern().formatted(registerKey.getMaterialName(material))));
    }

    public static class BlockModel {

        public static JsonElement cube(Material material, RegistryModule.Key registerKey) {
            String flagName = getResourceName(registerKey);
            boolean texturePresent = isTexturePresent(material, registerKey, "block");
            String texture = textureOrNull("%s:block/materials/%s/"+flagName, material, texturePresent);
            return ModelHelpers.simpleCubeAll(texture);
        }

        public static JsonElement pillar(Material material, RegistryModule.Key registerKey) {
            String flagName = getResourceName(registerKey);
            boolean endPresent = isTexturePresent(material, registerKey, "_end", "block");
            boolean sidePresent = isTexturePresent(material, registerKey, "_side", "block");
            String endTexture = textureOrNull("%s:block/materials/%s/"+flagName+"_end", material, endPresent);
            String sideTexture = textureOrNull("%s:block/materials/%s/"+flagName+"_side", material, sidePresent);
            return ModelHelpers.simplePillar(endTexture, sideTexture);
        }
    }

    public static class BlockState {

        public static JsonElement singleVariant(Material material, RegistryModule.Key registerKey) {
            return ModelHelpers.singleVariantBlockstate(material.getModId() + ":block/" + registerKey.idPattern().formatted(registerKey.getMaterialName(material)));
        }

        public static JsonElement axis(Material material, RegistryModule.Key registerKey) {
            return ModelHelpers.simpleAxisBlockstate(material.getModId() + ":block/" + registerKey.idPattern().formatted(registerKey.getMaterialName(material)));
        }
    }

    public static class ItemModel {

        public static JsonElement blockParent(Material material, RegistryModule.Key registerKey) {
            return ModelHelpers.simpleParentedModel(material.getModId() + ":block/" + registerKey.idPattern().formatted(registerKey.getMaterialName(material)));
        }
    }

    public static void toPack(BiConsumer<ResourceLocation, JsonElement> adder, ResourceLocation assetLocation, JsonElement assetJson) {
        adder.accept(assetLocation, assetJson);
    }
}
