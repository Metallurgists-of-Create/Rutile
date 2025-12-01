package dev.metallurgists.rutile.debug.material;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.metallurgists.rutile.api.material.Material;
import net.minecraft.resources.ResourceLocation;

public class MaterialJson {
    private final Material material;

    public MaterialJson(Material material) {
        this.material = material;
    }

    public ResourceLocation getName() {
        return material.getId();
    }

    public JsonElement json() {
        JsonObject jsonObject = new JsonObject();
        writeInfo(jsonObject);
        writeFlags(jsonObject);
        return jsonObject;
    }

    public void writeInfo(JsonObject json) {
        JsonObject jsonObject = new JsonObject();
        //MaterialInfo materialInfo = material.materialInfo();
        //jsonObject.addProperty("color", materialInfo.colour());
        //jsonObject.addProperty("melting_point", materialInfo.meltingPoint());
        //jsonObject.addProperty("composition", composition());
        json.add("info", jsonObject);
    }

    public void writeFlags(JsonObject json) {
        JsonObject jsonObject = new JsonObject();
        //MaterialFlags materialFlags = material.getFlags();
        //for (var c : materialFlags.getFlagContainers()) {
        //    JsonObject objects = new JsonObject();
        //    for (var obj : c.getObjects().entrySet()) {
        //        objects.addProperty(obj.getKey().rlForm(), obj.getValue().toString());
        //    }
        //    JsonObject builders = new JsonObject();
        //    for (var build : c.getBuilders().entrySet()) {
        //        var builder = build.getValue().setMaterialKey(material.getId());
        //        builders.addProperty(build.getKey().rlForm(), builder.getObjectId().toString());
        //    }
        //    jsonObject.add("objects", objects);
        //    jsonObject.add("builders", builders);
        //}
        json.add("flags", jsonObject);
    }
}
