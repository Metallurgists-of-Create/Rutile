package dev.metallurgists.rutile.api.data.server;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class RegistryAccessJsonReloadListener extends SimpleJsonResourceReloadListener {
    private static final List<RegistryAccessJsonReloadListener> INSTANCES = Collections.synchronizedList(new ArrayList<>());
    private @Nullable Map<ResourceLocation, JsonElement> jsonMap;

    @ApiStatus.Internal
    public static void runReloads(RegistryAccess access) {
        for(RegistryAccessJsonReloadListener listener : INSTANCES) {
            if (listener.jsonMap != null) {
                listener.parse(listener.jsonMap, access);
                listener.jsonMap = null;
            }
        }
    }

    protected RegistryAccessJsonReloadListener(Gson gson, String string) {
        super(gson, string);
        INSTANCES.add(this);
    }

    protected void apply(Map<ResourceLocation, JsonElement> jsonMap, ResourceManager resourceManager, ProfilerFiller profiler) {
        this.jsonMap = jsonMap;
    }

    public abstract void parse(Map<ResourceLocation, JsonElement> jsonMap, RegistryAccess registryAccess);
}
