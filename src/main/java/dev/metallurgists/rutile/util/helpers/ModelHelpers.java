package dev.metallurgists.rutile.util.helpers;

import com.google.gson.JsonObject;
import dev.metallurgists.rutile.api.dynamic_pack.asset.RutileDynamicResourcePack;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.flag.FlagKey;
import dev.metallurgists.rutile.api.material.flag.types.IItemRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

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

    public static void generatedItemModel(Material material, FlagKey<? extends IItemRegistry> itemRegistry) {
        ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
        String namespace = material.getNamespace();
        var flag = material.getFlag(itemRegistry);
        String flagName = itemRegistry.getId().getPath();
        boolean texturePresent = resourceManager.getResource(new ResourceLocation(namespace + ":textures/item/materials/" + material.getName() + "/" + flagName + ".png")).isPresent();
        String texture = texturePresent ? material.getNamespace() + ":item/materials/" + material.getName() + "/" + flagName : "rutile:item/materials/null/" + flagName;
        RutileDynamicResourcePack.addItemModel(new ResourceLocation(material.getNamespace(), flag.getIdPattern().formatted(material.getName())), ModelHelpers.simpleGeneratedModel("minecraft:item/generated", texture));
    }
}
