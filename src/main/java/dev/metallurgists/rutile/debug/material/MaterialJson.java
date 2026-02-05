package dev.metallurgists.rutile.debug.material;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.metallurgists.rutile.RutileClient;
import dev.metallurgists.rutile.api.composition.Composition;
import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.material.MaterialInfo;
import dev.metallurgists.rutile.api.material.flags.IMaterialFlag;
import dev.metallurgists.rutile.api.material.flags.MaterialFlags;
import dev.metallurgists.rutile.api.material.flags.StandaloneFlag;
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
        JsonObject flags = new JsonObject();
        for (var c : materialFlags.getFlagKeys()) {
            IMaterialFlag flag = material.getFlag(c);
            if (flag instanceof StandaloneFlag) {
                flags.addProperty(c.getKey().toString(), true);
            } else
                flags.add(c.getKey().toString(), flag.debugJson());
        }
        json.add("flags", flags);
    }
}
