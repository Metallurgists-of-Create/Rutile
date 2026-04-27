package dev.metallurgists.rutile.api.material.module.dynamic;

import dev.metallurgists.rutile.api.material.module.MaterialModule;
import dev.metallurgists.rutile.api.runtime.assets.RutileDynamicResourcePack;
import dev.metallurgists.rutile.api.runtime.data.RutileDynamicDataPack;

public interface DynamicPackModule<T extends DynamicPackModule<T>> extends MaterialModule<T> {

    default void addData(RutileDynamicDataPack dynamicPack) {}

    default void addAssets(RutileDynamicResourcePack dynamicPack) {}
}
