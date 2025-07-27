package dev.metallurgists.rutile.api.registry.material;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.registry.RutileMaterials;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

public class MaterialRegistryImpl extends MaterialRegistry {
    private static int networkIdCounter;

    private final int networkId = networkIdCounter++;
    private final java.lang.String modid;

    private boolean isRegistryClosed = false;
    @NotNull
    private Material fallbackMaterial;

    protected MaterialRegistryImpl(@NotNull java.lang.String modid) {
        super(modid);
        this.modid = modid;
    }

    @Override
    public void register(Material material) {
        this.register(material.getName(), material);
    }

    @Override
    public <T extends Material> T register(@NotNull java.lang.String key, @NotNull T value) {
        if (isRegistryClosed) {
            Rutile.LOGGER.error(
                    "Materials cannot be registered in the PostMaterialEvent (or after)! Must be added in the MaterialEvent. Skipping material {}...",
                    key);
            return null;
        }
        super.register(key, value);
        return value;
    }

    @NotNull
    @Override
    public Collection<Material> getAllMaterials() {
        return Collections.unmodifiableCollection(this.registry.values());
    }

    @Override
    public void setFallbackMaterial(@NotNull Material material) {
        this.fallbackMaterial = material;
    }

    @NotNull
    @Override
    public Material getFallbackMaterial() {
        return this.fallbackMaterial;
    }

    @Override
    public int getNetworkId() {
        return this.networkId;
    }

    @NotNull
    @Override
    public java.lang.String getModid() {
        return this.modid;
    }

    public void closeRegistry() {
        this.isRegistryClosed = true;
    }
}
