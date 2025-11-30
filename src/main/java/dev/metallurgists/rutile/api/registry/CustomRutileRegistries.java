package dev.metallurgists.rutile.api.registry;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.composition.data.ItemComposition;
import dev.metallurgists.rutile.api.composition.data.MaterialComposition;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class CustomRutileRegistries {
    public static final ResourceKey<Registry<ItemComposition>> ITEM_COMPOSITION_REGISTRY = makeRegistryKey(Rutile.id("composition/item"));
    public static final ResourceKey<Registry<MaterialComposition>> MATERIAL_COMPOSITION_REGISTRY = makeRegistryKey(Rutile.id("composition/material"));

    public static <T> ResourceKey<Registry<T>> makeRegistryKey(ResourceLocation registryId) {
        return ResourceKey.createRegistryKey(registryId);
    }
}
