package dev.metallurgists.rutile.debug.material;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.RutileApi;
import dev.metallurgists.rutile.api.element.Element;
import dev.metallurgists.rutile.api.registry.RutileRegistries;
import dev.metallurgists.rutile.debug.DebugPrinter;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DebugElementPrinter implements DebugPrinter {

    public static void print() {
        Path parent = Rutile.getGameDir().resolve("rutile/dumped/debug");
        Map<String, List<Element>> groupedElements = new HashMap<>();
        for (Element element : RutileRegistries.ELEMENTS) {
            groupedElements.compute(element.getModId(), (k, l) -> {
                if (l == null) l = new ArrayList<>();
                l.add(element);
                return l;
            });
        }
        ElementJson elementJson = new ElementJson(groupedElements);
        new DebugMaterialPrinter().writeJson(Rutile.id("elements"), "", parent, elementJson.json());
    }

    static class ElementJson {
        private final Map<String, List<Element>> elements;

        ElementJson(Map<String, List<Element>> elements) {
            this.elements = elements;
        }


        public JsonElement json() {
            JsonObject jsonObject = new JsonObject();
            for (Map.Entry<String, List<Element>> entry : elements.entrySet()) {
                JsonArray jsonArray = new JsonArray();
                for (Element element : entry.getValue()) {
                    jsonArray.add(element.getName() + " [" + element.getSymbol() + "]");
                }
                jsonObject.add(entry.getKey(), jsonArray);
            }
            return jsonObject;
        }
    }
}
