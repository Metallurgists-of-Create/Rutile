package dev.metallurgists.rutile.api.registry;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.material.MaterialData;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class MaterialRegistry extends RutileRegistry<MaterialData> {
    public MaterialRegistry(ResourceKey<? extends Registry<MaterialData>> key) {
        super(key);
    }

    @Override
    public MaterialData register(MaterialData data) {
        return register(Rutile.getResource(data.getName()), data);
    }

    @Override
    public MaterialData getByKey(ResourceLocation id) {
        return get(id);
    }

    public void onLoadComplete() {
        Rutile.LOGGER.info("Loaded {} materials", this.getAll().size());
    }
}
