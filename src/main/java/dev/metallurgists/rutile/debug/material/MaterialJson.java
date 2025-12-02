package dev.metallurgists.rutile.debug.material;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.metallurgists.rutile.RutileClient;
import dev.metallurgists.rutile.api.composition.Composition;
import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.material.MaterialInfo;
import dev.metallurgists.rutile.api.material.flags.MaterialFlags;
import net.createmod.catnip.lang.LangBuilder;
import net.minecraft.resources.ResourceLocation;

import static dev.metallurgists.rutile.api.composition.CompositionHandler.createTooltip;

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
        MaterialInfo materialInfo = material.getInfo();
        jsonObject.addProperty("color", materialInfo.getColour());
        Composition composition = materialInfo.getComposition();
        if (composition != null) {
            LangBuilder compositionName = RutileClient.lang();
            createTooltip(compositionName, composition);
            jsonObject.addProperty("composition", compositionName.string());
        }
        json.add("info", jsonObject);
    }

    public void writeFlags(JsonObject json) {
        MaterialFlags materialFlags = material.getFlags();
        JsonArray jsonArray = new JsonArray();
        for (var c : materialFlags.getFlagKeys()) {
            JsonObject flagDebug = material.getFlag(c).debugJson();
            flagDebug.addProperty("id", c.getKey());
            jsonArray.add(flagDebug);
        }
        json.add("flags", jsonArray);
    }
}
