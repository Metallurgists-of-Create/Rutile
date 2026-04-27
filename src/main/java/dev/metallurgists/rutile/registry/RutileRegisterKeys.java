package dev.metallurgists.rutile.registry;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.material.module.registry.DeferredKeys;
import dev.metallurgists.rutile.api.material.module.registry.KeyHolder;
import dev.metallurgists.rutile.api.material.module.registry.RegistryModule;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import java.util.function.Supplier;

public class RutileRegisterKeys {
    public static ResourceKey<Registry<RegistryModule.Key>> KEYS_KEY = ResourceKey.createRegistryKey(Rutile.id("register_keys"));
    public static final DeferredKeys KEYS = DeferredKeys.create(Rutile.ID);
    public static final Registry<RegistryModule.Key> KEYS_REGISTRY = KEYS.makeRegistry(builder -> builder.sync(true)
            .defaultKey(Rutile.id("null")));

    public static final KeyHolder<RegistryModule.Key> Null = create("null", "null_%s");

    public static final KeyHolder<RegistryModule.Key> Ingot = create("ingot", "%s_ingot");
    public static final KeyHolder<RegistryModule.Key> Nugget = create("nugget", "%s_nugget");
    public static final KeyHolder<RegistryModule.Key> Gem = create("gem", "%s");

    public static final KeyHolder<RegistryModule.Key> RawOre = create("raw_ore", "raw_%s");

    public static final KeyHolder<RegistryModule.Key> StorageBlock = create("storage_block", "%s_block");
    public static final KeyHolder<RegistryModule.Key> RawOreBlock = create("raw_ore_block", "raw_%s_block");


    public static KeyHolder<RegistryModule.Key> create(String name, String idPattern) {
        return create(name, () -> new RegistryModule.Key(Rutile.id(name), idPattern));
    }

    public static <T extends RegistryModule.Key> KeyHolder<T> create(String name, Supplier<T> supplier) {
        return KEYS.register(name, supplier);
    }
}
