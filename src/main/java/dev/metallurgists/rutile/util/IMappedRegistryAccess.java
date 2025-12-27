package dev.metallurgists.rutile.util;

import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public interface IMappedRegistryAccess<T> {

    default boolean rutile$isFrozen() {
        throw new AssertionError();
    }

    default ObjectList<Holder.Reference<T>> rutile$getById() {
        throw new AssertionError();
    }

    default Reference2IntMap<T> rutile$getToId() {
        throw new AssertionError();
    }

    default Map<ResourceLocation, Holder.Reference<T>> rutile$getByLocation() {
        throw new AssertionError();
    }

    default Map<ResourceKey<T>, Holder.Reference<T>> rutile$getByKey() {
        throw new AssertionError();
    }

    default Map<T, Holder.Reference<T>> rutile$getByValue() {
        throw new AssertionError();
    }

    default Map<ResourceKey<T>, RegistrationInfo> rutile$getRegistrationInfos() {
        throw new AssertionError();
    }
}
