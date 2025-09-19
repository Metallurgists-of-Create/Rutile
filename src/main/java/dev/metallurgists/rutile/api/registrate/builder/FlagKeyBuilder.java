package dev.metallurgists.rutile.api.registrate.builder;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonnullType;
import dev.metallurgists.rutile.api.composition.element.Element;
import dev.metallurgists.rutile.api.material.flag.FlagKey;
import dev.metallurgists.rutile.api.material.flag.IMaterialFlag;
import dev.metallurgists.rutile.registry.RutileRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.NotNull;

public class FlagKeyBuilder<C extends IMaterialFlag, T extends FlagKey<? extends IMaterialFlag>, P> extends AbstractBuilder<FlagKey<? extends IMaterialFlag>, T, P, FlagKeyBuilder<C, T, P>> {

    public static <C extends IMaterialFlag, T extends FlagKey<? extends IMaterialFlag>, P> FlagKeyBuilder<C, T, P> create(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, NonNullFunction<Class<C>, T> factory, Class<C> type) {
        return new FlagKeyBuilder<>(owner, parent, name, callback, factory, type);
    }

    private final NonNullFunction<Class<C>, T> factory;
    private final Class<C> type;

    protected FlagKeyBuilder(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, NonNullFunction<Class<C>, T> factory, Class<C> type) {
        super(owner, parent, name, callback, RutileRegistries.FLAG_KEY);
        this.factory = factory;
        this.type = type;
    }

    @Override
    protected @NonnullType T createEntry() {
        return factory.apply(type);
    }

    @Override
    protected RegistryEntry<FlagKey<? extends IMaterialFlag>, T> createEntryWrapper(@NotNull DeferredHolder<FlagKey<? extends IMaterialFlag>, T> delegate) {
        return new FlagKeyEntry<>(getOwner(), delegate);
    }

    @Override
    public FlagKeyEntry<T> register() {
        return (FlagKeyEntry<T>) super.register();
    }
}
