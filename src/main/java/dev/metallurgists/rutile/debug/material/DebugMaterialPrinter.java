package dev.metallurgists.rutile.debug.material;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.material.MaterialData;
import dev.metallurgists.rutile.debug.DebugPrinter;

import java.nio.file.Path;

public class DebugMaterialPrinter implements DebugPrinter {

    public static void print() {
        Path parent = Rutile.getGameDir().resolve("rutile/dumped/debug");
        //for (MaterialData data : RutileRegistries.MATERIALS) {
        //    DebugPrinter.writeJson(Rutile.getResource("materials"), data.getName(), parent, new MaterialJson(data).json());
        //}
    }

    static class MaterialJson {
        private final MaterialData data;

        MaterialJson(MaterialData data) {
            this.data = data;
        }

        public JsonElement json() {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("composition", data.getComposition().getString());
            return jsonObject;
        }
    }
}
