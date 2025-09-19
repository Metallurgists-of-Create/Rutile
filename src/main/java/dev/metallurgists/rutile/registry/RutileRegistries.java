package dev.metallurgists.rutile.registry;

import com.mojang.serialization.Lifecycle;
import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.composition.data.ItemComposition;
import dev.metallurgists.rutile.api.composition.data.MaterialComposition;
import dev.metallurgists.rutile.api.composition.element.Element;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.flag.FlagKey;
import dev.metallurgists.rutile.api.material.flag.IMaterialFlag;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class RutileRegistries {
    public static final ResourceKey<Registry<FlagKey<? extends IMaterialFlag>>> FLAG_KEY = createRegistryKey("flag_key");
    public static final ResourceKey<Registry<Element>> ELEMENT = createRegistryKey("element");
    public static final ResourceKey<Registry<Material>> MATERIAL = createRegistryKey("material");

    public static final ResourceKey<Registry<ItemComposition>> ITEM_COMPOSITION = createRegistryKey("composition/item");
    public static final ResourceKey<Registry<MaterialComposition>> MATERIAL_COMPOSITION = createRegistryKey("composition/material");


    public static final Registry<FlagKey<?>> FLAG_KEY_REGISTRY = makeSyncedRegistry(FLAG_KEY);
    public static final Registry<Element> ELEMENT_REGISTRY = makeSyncedRegistry(ELEMENT);
    public static final Registry<Material> MATERIAL_REGISTRY = makeSyncedRegistry(MATERIAL);


    private static <T> ResourceKey<Registry<T>> createRegistryKey(String name) {
        return ResourceKey.createRegistryKey(Rutile.id(name));
    }

    /**
     * Creates a {@link Registry} that get synchronised to clients.
     *
     * @param <T> the entry of the registry.
     */
    private static <T> Registry<T> makeSyncedRegistry(ResourceKey<Registry<T>> registryKey) {
        return new RegistryBuilder<>(registryKey).sync(true).create();
    }

    /**
     * Creates a simple {@link Registry} that <B>won't</B> be synced to clients.
     *
     * @param <T> the entry of the registry.
     */
    private static <T> Registry<T> makeRegistry(ResourceKey<Registry<T>> registryKey) {
        return new RegistryBuilder<>(registryKey).create();
    }
    private static <T> Registry<T> registerSimpleWithIntrusiveHolders(ResourceKey<? extends Registry<T>> registryKey) {
        return new MappedRegistry<>(registryKey, Lifecycle.stable(), true);
    }

    public static void register() {

    }
}
