package dev.metallurgists.rutile.api.material.builder;

import lombok.Getter;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class FlagContainer<T> {

    @Getter
    private Map<FlagSource<T>, ResourceLocation> objects = new HashMap<>();
    @Getter
    private Map<FlagSource<T>, MaterialRegistryBuilder<T>> builders = new HashMap<>();

    @Getter
    private final FlagRegistryType<T> registryType;

    public FlagContainer(FlagRegistryType<T> registryType) {
        this.registryType = registryType;
    }

    public void add(FlagSource<T> flagSource, ResourceLocation resourceLocation) {
        this.objects.put(flagSource, resourceLocation);
    }

    public void add(FlagSource<T> flagSource, MaterialRegistryBuilder<T> builder) {
        this.builders.put(flagSource, builder);
    }

    public abstract Registry<T> getRegistry();

    public List<FlagSource<T>> getFlags() {
        return objects.keySet().stream().toList();
    }

    @Nullable
    public MaterialRegistryBuilder<T> getBuilder(FlagSource<T> flagSource) {
        return builders.get(flagSource);
    }

    public ResourceLocation get(FlagSource<T> flagSource) {
        return objects.get(flagSource);
    }

    public T getObject(FlagSource<T> flagSource) {
        return get(ResourceKey.create(registryType.resourceKey(), get(flagSource)));
    }

    public T get(ResourceLocation location) {
        return get(ResourceKey.create(registryType.resourceKey(), location));
    }

    public abstract T get(ResourceKey<T> registryKey);
}
