package dev.metallurgists.rutile.api.data.server.manager.composition;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.serialization.JsonOps;
import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.composition.Composition;
import dev.metallurgists.rutile.api.data.AbstractReloadManager;
import lombok.Getter;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.List;
import java.util.Map;

public abstract class AbstractCompositionManager<T> extends AbstractReloadManager {

    @Getter
    private final ResourceLocation type;

    @Getter
    private final ResourceKey<Registry<T>> typeRegistry;

    public AbstractCompositionManager(ResourceLocation type,  ResourceKey<Registry<T>> typeRegistry) {
        super("composition/" + (type.getNamespace().equals(Rutile.ID) ? type.getPath() : type.getNamespace() + "/" + type.getPath()));
        this.type = type;
        this.typeRegistry = typeRegistry;
    }

    public abstract Map<T, Composition> getCompositions();

    public abstract List<T> getComposed();

    public abstract void clearData();

    public abstract void putComposition(T composed, Composition composition);

    public abstract T getFromKey(ResourceLocation key);

    @Override
    public void parse(Map<ResourceLocation, JsonElement> jsonMap, RegistryAccess registryAccess) {
        clearData();

        for(Map.Entry<ResourceLocation, JsonElement> entry : jsonMap.entrySet()) {
            ResourceLocation resourceLocation = entry.getKey();

            if (resourceLocation.getPath().startsWith("_")) {
                continue;
            }

            try {
                T composed = getFromKey(resourceLocation);
                if (composed != null) {
                    Composition composition = Composition.CODEC.parse(RegistryOps.create(JsonOps.INSTANCE, registryAccess), entry.getValue()).getOrThrow();
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
