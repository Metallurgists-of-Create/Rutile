package dev.metallurgists.rutile.util.helpers;

import com.google.gson.JsonObject;
import dev.metallurgists.rutile.api.dynamic_pack.asset.RutileDynamicResourcePack;
import dev.metallurgists.rutile.api.material.base.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

import java.util.List;

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

    public static void generatedItemModel(Material material, ResourceLocation builderName, String nameFormat) {
        ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
        String namespace = material.getNamespace();
        String flagName = (defaultFlagSources.contains(builderName.getNamespace()) ? "" : builderName.getNamespace() + "_") + builderName.getPath();
        boolean texturePresent = resourceManager.getResource(ResourceLocation.fromNamespaceAndPath(namespace, "textures/item/materials/" + material.getName() + "/" + flagName + ".png")).isPresent();
        String texture = texturePresent ? material.getNamespace() + ":item/materials/" + material.getName() + "/" + flagName : "rutile:item/materials/null/" + flagName;
        RutileDynamicResourcePack.addItemModel(ResourceLocation.fromNamespaceAndPath(material.getNamespace(), nameFormat.formatted(material.getName())), ModelHelpers.simpleGeneratedModel("minecraft:item/generated", texture));
    }

    public static void cubeAllBlockModel(Material material, ResourceLocation builderName, String nameFormat) {
        ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
        String namespace = material.getNamespace();
        String flagName = (defaultFlagSources.contains(builderName.getNamespace()) ? "" : builderName.getNamespace() + "_") + builderName.getPath();
        boolean texturePresent = resourceManager.getResource(ResourceLocation.fromNamespaceAndPath(namespace, "textures/block/materials/" + material.getName() + "/" + flagName + ".png")).isPresent();
        String texture = texturePresent ? material.getNamespace() + ":block/materials/" + material.getName() + "/" + flagName : "metallurgica:block/materials/null/" + flagName;
        RutileDynamicResourcePack.addBlockModel(ResourceLocation.fromNamespaceAndPath(namespace, nameFormat.formatted(material.getName())), ModelHelpers.simpleCubeAll(texture));
        RutileDynamicResourcePack.addBlockState(ResourceLocation.fromNamespaceAndPath(namespace, nameFormat.formatted(material.getName())), ModelHelpers.singleVariantBlockstate(material.getNamespace() + ":block/" + nameFormat.formatted(material.getName())));
        RutileDynamicResourcePack.addItemModel(ResourceLocation.fromNamespaceAndPath(namespace, nameFormat.formatted(material.getName())), ModelHelpers.simpleParentedModel(material.getNamespace() + ":block/" + nameFormat.formatted(material.getName())));
    }
}
