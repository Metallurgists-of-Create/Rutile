package dev.metallurgists.rutile.api.material.module.registry;

import dev.metallurgists.rutile.registry.RutileRegisterKeys;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

public class DeferredKeys extends DeferredRegister<RegistryModule.Key> {

    protected DeferredKeys(String namespace) {
        super(RutileRegisterKeys.KEYS_KEY, namespace);
    }

    public static DeferredKeys create(String modid) {
        return new DeferredKeys(modid);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <I extends RegistryModule.Key> @NotNull KeyHolder<I> register(@NotNull String name, @NotNull Function<ResourceLocation, ? extends I> func) {
        return (KeyHolder<I>) super.register(name, func);
    }

    @Override
    public <I extends RegistryModule.Key> @NotNull KeyHolder<I> register(@NotNull String name, @NotNull Supplier<? extends I> sup) {
        return this.register(name, key -> sup.get());
    }

    @Override
    protected <I extends RegistryModule.Key> @NotNull KeyHolder<I> createHolder(@NotNull ResourceKey<? extends Registry<RegistryModule.Key>> registryKey, @NotNull ResourceLocation key) {
        return new KeyHolder<>(ResourceKey.create(registryKey, key));
    }
}
