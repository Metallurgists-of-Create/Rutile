package dev.metallurgists.rutile.util.helpers;

import com.google.gson.JsonObject;

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
}
