package dev.metallurgists.rutile.api.material.module;

import dev.metallurgists.rutile.registry.RutileModules;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

public class DeferredModules extends DeferredRegister<MaterialModule<?>> {

    protected DeferredModules(String namespace) {
        super(RutileModules.MODULES_KEY, namespace);
    }

    public static DeferredModules create(String modid) {
        return new DeferredModules(modid);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <I extends MaterialModule<?>> @NotNull ModuleHolder<I> register(@NotNull String name, @NotNull Function<ResourceLocation, ? extends I> func) {
        return (ModuleHolder<I>) super.register(name, func);
    }

    @Override
    public <I extends MaterialModule<?>> @NotNull ModuleHolder<I> register(@NotNull String name, @NotNull Supplier<? extends I> sup) {
        return this.register(name, key -> sup.get());
    }

    @Override
    protected <I extends MaterialModule<?>> @NotNull ModuleHolder<I> createHolder(@NotNull ResourceKey<? extends Registry<MaterialModule<?>>> registryKey, @NotNull ResourceLocation key) {
        return new ModuleHolder<>(ResourceKey.create(registryKey, key));
    }
}
