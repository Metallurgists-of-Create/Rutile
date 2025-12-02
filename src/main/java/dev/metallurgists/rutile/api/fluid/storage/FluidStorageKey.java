package dev.metallurgists.rutile.api.fluid.storage;

import dev.metallurgists.rutile.api.fluid.FluidState;
import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.tag.TagUtil;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

public class FluidStorageKey {
    private static final Map<ResourceLocation, FluidStorageKey> keys = new Object2ObjectOpenHashMap<>();

    @Getter
    private final ResourceLocation resourceLocation;
    @Getter
    private final @Nullable TagKey<Fluid> extraTag;
    @Getter
    private final Function<Material, String> registryNameFunction;
    private final Function<Material, String> translationKeyFunction;
    private final int hashCode;
    @Getter
    private final @Nullable FluidState defaultFluidState;
    @Getter
    private final int registrationPriority;

    public FluidStorageKey(@NotNull ResourceLocation resourceLocation, @Nullable TagKey<Fluid> extraTag,
                           @NotNull Function<@NotNull Material, @NotNull String> registryNameFunction,
                           @NotNull Function<@NotNull Material, @NotNull String> translationKeyFunction,
                           @Nullable FluidState defaultFluidState, int registrationPriority) {
        this.resourceLocation = resourceLocation;
        this.extraTag = extraTag;
        this.registryNameFunction = registryNameFunction;
        this.translationKeyFunction = translationKeyFunction;
        this.hashCode = resourceLocation.hashCode();
        this.defaultFluidState = defaultFluidState;
        this.registrationPriority = registrationPriority;
        if (keys.containsKey(resourceLocation)) {
            throw new IllegalArgumentException("Cannot create duplicate keys");
        }
        keys.put(resourceLocation, this);
    }

    public FluidStorageKey(@NotNull ResourceLocation resourceLocation, @NotNull String tagKey,
                           @NotNull Function<@NotNull Material, @NotNull String> registryNameFunction,
                           @NotNull Function<@NotNull Material, @NotNull String> translationKeyFunction,
                           @Nullable FluidState defaultFluidState, int registrationPriority) {
        this(resourceLocation, TagUtil.createFluidTag(tagKey),
                registryNameFunction, translationKeyFunction,
                defaultFluidState, registrationPriority);
    }

    public FluidStorageKey(@NotNull ResourceLocation resourceLocation,
                           @NotNull Function<@NotNull Material, @NotNull String> registryNameFunction,
                           @NotNull Function<@NotNull Material, @NotNull String> translationKeyFunction,
                           @Nullable FluidState defaultFluidState, int registrationPriority) {
        this(resourceLocation, (TagKey<Fluid>) null,
                registryNameFunction, translationKeyFunction,
                defaultFluidState, registrationPriority);
    }

    public static @Nullable FluidStorageKey getByName(@NotNull ResourceLocation location) {
        return keys.get(location);
    }

    public static Collection<FluidStorageKey> allKeys() {
        return keys.values();
    }

    public @NotNull String getRegistryNameFor(@NotNull Material baseName) {
        return registryNameFunction.apply(baseName);
    }

    public @NotNull String getTranslationKeyFor(@NotNull Material material) {
        return this.translationKeyFunction.apply(material);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FluidStorageKey fluidKey = (FluidStorageKey) o;

        return resourceLocation.equals(fluidKey.getResourceLocation());
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    @Override
    public @NotNull String toString() {
        return "FluidStorageKey{" + resourceLocation + '}';
    }
}
