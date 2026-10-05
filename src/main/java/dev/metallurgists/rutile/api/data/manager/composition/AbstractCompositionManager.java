package dev.metallurgists.rutile.api.data.manager.composition;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.composition.Composition;
import dev.metallurgists.rutile.api.data.AbstractReloadManager;
import lombok.Getter;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.TagsUpdatedEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractCompositionManager<T> extends AbstractReloadManager {
    @Getter
    private final ResourceLocation type;

    @Getter
    private final ResourceKey<Registry<T>> registryKey;

    @Getter
    private final Registry<T> registry;

    @Getter
    private final Map<T, Composition<T>> compositions = new HashMap<>();

    @Getter
    private final List<T> composed = new ArrayList<>();

    @Getter
    private final Codec<Composition<T>> codec;

    private final Map<T, Composition<T>> compositionsCache = new HashMap<>();
    private final List<Composition<T>> parsedCompositions = new ArrayList<>();

    public AbstractCompositionManager(ResourceLocation type, ResourceKey<Registry<T>> registryKey, Registry<T> registry) {
        super(folder(type));
        this.type = type;
        this.registryKey = registryKey;
        this.registry = registry;
        this.codec = Composition.codec(registryKey, registry.byNameCodec());
        NeoForge.EVENT_BUS.addListener(TagsUpdatedEvent.class, event -> {
            if (event.getUpdateCause() == TagsUpdatedEvent.UpdateCause.SERVER_DATA_LOAD) {
                resolveParsed();
            }
        });
    }

    private static String folder(ResourceLocation type) {
        return "composition/" + (type.getNamespace().equals(Rutile.ID) ? type.getPath() : type.getNamespace() + "/" + type.getPath());
    }

    public void clearData() {
        this.compositions.clear();
        this.composed.clear();
    }

    public void putComposition(T composed, Composition<T> composition) {
        // Only track the key once so overriding an entry doesn't list it twice
        if (this.compositions.put(composed, composition) == null) {
            this.composed.add(composed);
        }
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> files, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        parsedCompositions.clear();
        for (Map.Entry<ResourceLocation, JsonElement> entry : files.entrySet()) {
            ResourceLocation resourceLocation = entry.getKey();

            if (resourceLocation.getPath().startsWith("_")) {
                continue;
            }

            try {
                parsedCompositions.add(codec.parse(JsonOps.INSTANCE, entry.getValue()).getOrThrow());
            } catch (RuntimeException runtimeException) {
                Rutile.LOGGER.error("Parsing error loading {} composition {}", type, resourceLocation, runtimeException);
            }
        }
        resolveParsed();
    }

    private void resolveParsed() {
        clearData();
        compositionsCache.clear();
        for (Composition<T> composition : parsedCompositions) {
            for (T content : composition.contents().resolve(registry)) {
                if (content != null) {
                    putComposition(content, composition);
                }
            }
        }
        Rutile.LOGGER.info("Load Complete for {} {} compositions", composed.size(), type);
    }

    public Composition<T> getComposition(T value) {
        return compositionsCache.computeIfAbsent(value, compositions::get);
    }

    public boolean hasComposition(T value) {
        return compositions.containsKey(value);
    }
}