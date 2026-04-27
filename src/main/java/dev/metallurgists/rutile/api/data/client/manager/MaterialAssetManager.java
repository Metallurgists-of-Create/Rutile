package dev.metallurgists.rutile.api.data.client.manager;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.JsonOps;
import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.data.AbstractReloadManager;
import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.material.module.registry.RegistryModule;
import dev.metallurgists.rutile.api.material.registry.asset.MaterialAsset;
import dev.metallurgists.rutile.registry.RutileRegisterKeys;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MaterialAssetManager extends AbstractReloadManager {
    public static MaterialAssetManager INSTANCE = new MaterialAssetManager();

    @Getter
    public Map<PrefixedKey, MaterialAsset> assets = new HashMap<>();
    @Getter
    public List<PrefixedKey> prefixedKeys = new ArrayList<>();

    public MaterialAssetManager() {
        super("material_assets");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> files, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        this.assets.clear();
        this.prefixedKeys.clear();

        for(Map.Entry<ResourceLocation, JsonElement> entry : files.entrySet()) {
            ResourceLocation resourceLocation = entry.getKey();

            if (resourceLocation.getPath().startsWith("_")) {
                continue;
            }

            try {
                var pair = prefixAndMaterial(resourceLocation);
                PrefixedKey prefixedKey = new PrefixedKey(pair.getFirst(), pair.getSecond());
                MaterialAsset asset = MaterialAsset.CODEC.parse(JsonOps.INSTANCE, entry.getValue()).getOrThrow();
                if (asset != null) {
                    this.assets.put(prefixedKey, asset);
                    this.prefixedKeys.add(prefixedKey);
                }
            } catch (IllegalArgumentException | JsonParseException jsonParseException) {
                Rutile.LOGGER.error("Parsing error loading material asset {}", resourceLocation, jsonParseException);
            }
        }
        Rutile.LOGGER.info("Load Complete for {} material assets", getPrefixedKeys().size());
    }

    public Pair<RegistryModule.Key, ResourceLocation> prefixAndMaterial(ResourceLocation resourceLocation) {
        // FORMAT: rutile:rutile/material_assets/iron/rutile_ingot => rutile:iron | rutile:ingot
        String materialNamespace = resourceLocation.getNamespace();
        String[] pathElements = resourceLocation.getPath().replace(".json", "").split("/");
        String prefix = pathElements[pathElements.length - 1];
        String[] prefixParts = prefix.split("_", 2);
        RegistryModule.Key key = RutileRegisterKeys.KEYS_REGISTRY.get(ResourceLocation.fromNamespaceAndPath(prefixParts[0], prefixParts[1]));
        String materialName = pathElements[pathElements.length - 2];
        ResourceLocation materialLoc = ResourceLocation.fromNamespaceAndPath(materialNamespace, materialName);
        return new Pair<>(key, materialLoc);
    }

    public static MaterialAssetManager getInstance() {
        return INSTANCE;
    }

    public static void register(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(getInstance());
    }

    public boolean hasAsset(RegistryModule.Key registerKey, Material material) {
        return getAsset(registerKey, material) != null;
    }

    public MaterialAsset getAsset(RegistryModule.Key registerKey, Material material) {
        PrefixedKey prefixedKey = new PrefixedKey(registerKey, material.getId());
        return this.assets.get(prefixedKey);
    }

    public record PrefixedKey(RegistryModule.Key registerKey, ResourceLocation material) {

    }
}
