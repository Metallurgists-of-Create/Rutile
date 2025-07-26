package dev.metallurgists.rutile.api.material.registry.item;

import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.flag.types.IItemRegistry;

public interface IMaterialItem {
    Material getMaterial();

    IItemRegistry getFlag();
}
