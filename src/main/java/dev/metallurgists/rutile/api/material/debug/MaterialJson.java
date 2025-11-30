package dev.metallurgists.rutile.api.material.debug;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.builder.MaterialFlags;
import dev.metallurgists.rutile.util.ClientUtil;
import net.createmod.catnip.lang.LangBuilder;

import static dev.metallurgists.rutile.api.composition.CompositionTooltipHandler.createTooltip;

public class MaterialJson {
    private final Material material;

    public MaterialJson(Material material) {
        this.material = material;
    }

    public String getName() {
        return material.getNamespace() + "_" + material.getName();
    }

    public JsonElement json() {
        JsonObject jsonObject = new JsonObject();
        writeInfo(jsonObject);
        writeFlags(jsonObject);
        return jsonObject;
    }

    public void writeInfo(JsonObject json) {
        JsonObject jsonObject = new JsonObject();
        Material.MaterialInfo materialInfo = material.materialInfo();
        jsonObject.addProperty("color", materialInfo.colour());
        jsonObject.addProperty("melting_point", materialInfo.meltingPoint());
        jsonObject.addProperty("composition", composition());
        json.add("info", jsonObject);
    }

    public void writeFlags(JsonObject json) {
        JsonObject jsonObject = new JsonObject();
        MaterialFlags materialFlags = material.getFlags();
        for (var c : materialFlags.getFlagContainers()) {
            JsonObject objects = new JsonObject();
            for (var obj : c.getObjects().entrySet()) {
                objects.addProperty(obj.getKey().rlForm(), obj.getValue().toString());
            }
            JsonObject builders = new JsonObject();
            for (var build : c.getBuilders().entrySet()) {
                var builder = build.getValue().setMaterialKey(material.getId());
                builders.addProperty(build.getKey().rlForm(), builder.getObjectId().toString());
            }
            jsonObject.add("objects", objects);
            jsonObject.add("builders", builders);
        }
        json.add("flags", jsonObject);
    }

    public String composition() {
        LangBuilder compositionName = ClientUtil.lang();
        var comp = material.getComposition();
        createTooltip(compositionName, comp);
        return compositionName.string();
    }
}
