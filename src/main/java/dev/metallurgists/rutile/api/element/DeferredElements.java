package dev.metallurgists.rutile.api.element;

import dev.metallurgists.rutile.registry.RutileRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

public class DeferredElements extends DeferredRegister<Element> {

    protected DeferredElements(String namespace) {
        super(RutileRegistries.ELEMENTS, namespace);
    }

    public static DeferredElements create(String modid) {
        return new DeferredElements(modid);
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NotNull <I extends Element> ElementHolder<I> register(@NotNull String name, @NotNull Function<ResourceLocation, ? extends I> func) {
        return (ElementHolder<I>) super.register(name, func);
    }

    @Override
    public @NotNull <I extends Element> ElementHolder<I> register(@NotNull String name, @NotNull Supplier<? extends I> sup) {
        return this.register(name, key -> sup.get());
    }

    @Override
    protected @NotNull <I extends Element> ElementHolder<I> createHolder(@NotNull ResourceKey<? extends Registry<Element>> registryKey, @NotNull ResourceLocation key) {
        return new ElementHolder<>(ResourceKey.create(registryKey, key));
    }
}
