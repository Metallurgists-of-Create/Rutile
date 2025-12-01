package dev.metallurgists.rutile.debug.material;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.RutileApi;
import dev.metallurgists.rutile.api.element.Element;
import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.tag.TagPrefix;
import dev.metallurgists.rutile.api.tag.TagType;
import dev.metallurgists.rutile.debug.DebugPrinter;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class DebugTagPrefixPrinter implements DebugPrinter {

    public static void print() {
        Path parent = Rutile.getGameDir().resolve("rutile/dumped/debug");
        for (String modid : RutileApi.getTagPrefixRegistry().getUsedNamespaces()) {
            List<TagPrefix> tagPrefixes = RutileApi.getTagPrefixRegistry().getAll().stream().filter(t -> t.id().getNamespace().equals(modid)).toList();
            TagPrefixJson tagPrefixJson = new TagPrefixJson(tagPrefixes);
            new DebugMaterialPrinter().writeJson(Rutile.id(modid), "tag_prefixes", parent, tagPrefixJson.json());
        }
    }

    static class TagPrefixJson {
        private final List<TagPrefix> tagPrefixes;

        public TagPrefixJson(List<TagPrefix> tagPrefixes) {
            this.tagPrefixes = tagPrefixes;
        }

        public JsonElement json() {
            JsonObject jsonObject = new JsonObject();
            for (TagPrefix tagPrefix : tagPrefixes) {
                JsonObject prefixObject = new JsonObject();
                String generationType = tagPrefix.doGenerateBlock() ? "block" : tagPrefix.doGenerateItem() ? "item" : null;
                if (generationType != null) prefixObject.addProperty("generation_type", generationType);
                prefixObject.addProperty("unification_enabled", tagPrefix.unificationEnabled());
                JsonObject materialAmountsObject = new JsonObject();
                var amountKeys = tagPrefix.materialAmounts().keySet();
                if (amountKeys.isEmpty()) {
                    materialAmountsObject.addProperty("all", tagPrefix.materialAmount());
                } else {
                    for (Material material : tagPrefix.materialAmounts().keySet()) {
                        materialAmountsObject.addProperty(material.getId().toString(), tagPrefix.getMaterialAmount(material));
                    }
                    materialAmountsObject.addProperty("else", tagPrefix.materialAmount());
                }
                prefixObject.add("material_amounts", materialAmountsObject);
                JsonArray ignoredArray = new JsonArray();
                for (Material material : tagPrefix.getIgnored().keySet()) {
                    ignoredArray.add(material.getId().toString());
                }
                prefixObject.add("ignored_materials", ignoredArray);
                jsonObject.add(tagPrefix.getName(), prefixObject);
            }
            return jsonObject;
        }
    }
}
