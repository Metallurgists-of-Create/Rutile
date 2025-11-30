package dev.metallurgists.rutile.api.material.registry.item;

import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.builder.MaterialItemBuilder;

public interface IMaterialItem {
    Material getMaterial();

    MaterialItemBuilder getBuilder();
}
