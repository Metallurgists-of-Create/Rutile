package dev.metallurgists.rutile.api.material.registry.block;

import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.builder.MaterialBlockBuilder;

public interface IMaterialBlock {
    Material getMaterial();

    MaterialBlockBuilder getBuilder();
}
