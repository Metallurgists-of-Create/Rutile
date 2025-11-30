package dev.metallurgists.rutile.api.material.debug;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.registry.RutileAPI;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class DebugMaterialPrinter {

    @ApiStatus.Internal
    public static void writeJson(ResourceLocation id, @Nullable String subDir, Path parent, byte[] json) {
        try {
            Path file;
            if (subDir != null) {
                // assume JSON
                file = parent.resolve(id.getNamespace()).resolve(subDir).resolve(id.getPath() + ".json");
            } else {
                // assume the file type is also appended if a full path is given.
                file = parent.resolve(id.getNamespace()).resolve(id.getPath());
            }
            Files.createDirectories(file.getParent());
            try (OutputStream output = Files.newOutputStream(file)) {
                output.write(json);
            }
        } catch (IOException e) {
            Rutile.LOGGER.error("Failed to write JSON export for file {}", id, e);
        }
    }

    public static void print() {
        Path parent = Rutile.getGameDir().resolve("rutile/dumped/debug");
        for (Material material : RutileAPI.getMaterialRegistry().getAll()) {
            MaterialJson materialJson = new MaterialJson(material);
            byte[] materialBytes = materialJson.json().toString().getBytes(StandardCharsets.UTF_8);
            writeJson(material.getId(), "materials", parent, materialBytes);
        }
    }
}
