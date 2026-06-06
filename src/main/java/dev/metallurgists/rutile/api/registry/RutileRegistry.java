package dev.metallurgists.rutile.api.registry;

import com.mojang.serialization.Lifecycle;
import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class RutileRegistry<T> extends MappedRegistry<T> implements IRutileRegistry<T> {

    private final Set<String> usedNamespaces = new HashSet<>();

    private boolean isRegistryClosed = false;

    public RutileRegistry(ResourceKey<? extends Registry<T>> key) {
        super(key, Lifecycle.stable());
    }

    @Override
    public Set<String> getUsedNamespaces() {
        return Collections.unmodifiableSet(usedNamespaces);
    }

    @Override
    public abstract T register(T object);

    T register(ResourceLocation id, T object) {
        this.register(ResourceKey.create(this.key(), id), object, RegistrationInfo.BUILT_IN);
        return object;
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public boolean doesSync() {
        return true;
    }

    @Override
    public List<T> getAll() {
        return stream().toList();
    }

    @Override
    public abstract T getByKey(ResourceLocation id);

    @Override
    public Holder.@NotNull Reference<T> register(int id, @NotNull ResourceKey<T> key, @NotNull T value, @NotNull RegistrationInfo registrationInfo) {
        if (isRegistryClosed) {
            throw new IllegalStateException("Objects cannot be registered! Must be added in the RegisterEvent. Skipping object %s...".formatted(key.location()));
        }
        usedNamespaces.add(key.location().getNamespace());
        return super.register(id, key, value, registrationInfo);
    }

    @Override
    public boolean isFrozen() {
        return this.rutile$isFrozen();
    }

    @Override
    public abstract void onLoadComplete();

    public void close() {
        isRegistryClosed = true;
    }
}
