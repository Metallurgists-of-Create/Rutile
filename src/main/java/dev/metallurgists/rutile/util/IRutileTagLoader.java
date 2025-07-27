package dev.metallurgists.rutile.util;

import net.minecraft.core.Registry;
import org.jetbrains.annotations.Nullable;

public interface IRutileTagLoader<T> {

    void rutile$setRegistry(Registry<T> registry);

    @Nullable
    Registry<T> rutile$getRegistry();
}
