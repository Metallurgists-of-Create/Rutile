package dev.metallurgists.rutile.debug.material;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.RutileApi;
import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.registry.RutileRegistries;
import dev.metallurgists.rutile.debug.DebugPrinter;

import java.nio.file.Path;

public class DebugMaterialPrinter implements DebugPrinter {
    public static void print() {
        Path parent = Rutile.getGameDir().resolve("rutile/dumped/debug");
        for (Material material : RutileRegistries.MATERIALS) {
            MaterialJson materialJson = new MaterialJson(material);
            new DebugMaterialPrinter().writeJson(materialJson.getName(), "materials", parent, materialJson.json());
        }
    }
}
