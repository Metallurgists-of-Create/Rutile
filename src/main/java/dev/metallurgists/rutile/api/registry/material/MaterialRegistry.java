package dev.metallurgists.rutile.api.registry.material;

import com.mojang.serialization.Lifecycle;
import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.registry.RutileMaterials;
import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Stream;

public class MaterialRegistry extends MappedRegistry<Material> {

    private final Set<String> usedNamespaces = new HashSet<>();
    private final Map<String, Material> fallbackMaterials = new HashMap<>();

    private boolean isRegistryClosed = false;

    public MaterialRegistry(ResourceKey<Registry<Material>> key) {
        super(key, Lifecycle.stable());
    }

    public @NotNull Set<String> getUsedNamespaces() {
        return Collections.unmodifiableSet(usedNamespaces);
    }

    @Override
    public @NotNull Stream<Material> stream() {
        return super.stream();
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public boolean doesSync() {
        return true;
    }

    public Material register(Material material) {
        return register(material.getId(), material);
    }

    private Material register(ResourceLocation id, Material material) {
        this.register(ResourceKey.create(this.key(), id), material, RegistrationInfo.BUILT_IN);
        return material;
    }

    public Material getMaterial(String name) {
        return getMaterial(Rutile.id(name));
    }

    public Material getMaterial(ResourceLocation name) {
        Material value = get(name);
        return value != null ? value : RutileMaterials.Null;
    }

    @Override
    public ResourceLocation getKey(Material material) {
        return material.getId();
    }

    @Override
    public Holder.@NotNull Reference<Material> register(int id,
                                                        @NotNull ResourceKey<Material> key, @NotNull Material value,
                                                        @NotNull RegistrationInfo registrationInfo) {
        if (isRegistryClosed) {
            throw new IllegalStateException(
                    "Materials cannot be registered in the PostMaterialEvent (or after)! Must be added in the RegisterEvent. Skipping material %s..."
                            .formatted(key.location()));
        }
        usedNamespaces.add(key.location().getNamespace());
        return super.register(id, key, value, registrationInfo);
    }

    public void setFallbackMaterial(@NotNull String modId, @NotNull Material material) {
        fallbackMaterials.put(modId, material);
    }

    @NotNull
    public Material getFallbackMaterial(@NotNull String modId) {
        return fallbackMaterials.getOrDefault(modId, getDefaultFallback());
    }

    public Material getDefaultFallback() {
        return fallbackMaterials.get(Rutile.ID);
    }

    public boolean isFrozen() {
        return this.rutile$isFrozen();
    }

    public void close() {
        isRegistryClosed = true;
    }
}
