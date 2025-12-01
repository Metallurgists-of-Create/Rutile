package dev.metallurgists.rutile.api.data.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.metallurgists.rutile.Rutile;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;

public abstract class AbstractReloadManager extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public AbstractReloadManager(String filePath) {
        super(GSON, Rutile.ID + "/" + filePath);
    }

    public static ResourceLocation extractKey(ResourceLocation resourceLocation) {
        return ResourceLocation.fromNamespaceAndPath(resourceLocation.getNamespace(), removeExtension(resourceLocation).replace(".json", ""));
    }

    public static String removeExtension(ResourceLocation resourceLocation) {
        String path = resourceLocation.getPath(); // Get the full path from ResourceLocation
        String[] pathElements = path.split("/");
        return pathElements[pathElements.length - 1];
    }
}
