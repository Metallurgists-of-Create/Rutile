package dev.metallurgists.rutile.api.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.serialization.JsonOps;
import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.composition.Composition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.List;
import java.util.Map;

public abstract class AbstractCompositionManager<T> extends AbstractReloadManager {

    private final String type;

    public AbstractCompositionManager(String type) {
        super("composition/" + type);
        this.type = type;
    }

    public abstract Map<T, Composition> getCompositions();

    public abstract List<T> getComposed();

    public abstract void clearData();

    public abstract void putComposition(T composed, Composition composition);

    public abstract T getFromKey(ResourceLocation key);

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> files, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        clearData();

        for(Map.Entry<ResourceLocation, JsonElement> entry : files.entrySet()) {
            ResourceLocation resourceLocation = entry.getKey();

            if (resourceLocation.getPath().startsWith("_")) {
                continue;
            }

            try {
                T composed = getFromKey(resourceLocation);
                if (composed != null) {
                    Composition composition = Composition.CODEC.parse(JsonOps.INSTANCE, entry.getValue()).getOrThrow();
                    if (composition != null) {
                        putComposition(composed, composition);
                    }
                }
            } catch (IllegalArgumentException | JsonParseException jsonParseException) {
                Rutile.LOGGER.error("Parsing error loading {} composition {}", type, resourceLocation, jsonParseException);
            }
        }
        Rutile.LOGGER.info("Load Complete for {} {} compositions", getComposed().size(), type);
    }

    public Composition getComposition(T value) {
        return getCompositions().get(value);
    }

    public boolean hasComposition(T value) {
        return getComposed().contains(value);
    }
}
