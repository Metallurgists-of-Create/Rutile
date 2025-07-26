package dev.metallurgists.rutile.api.material.registry.block;

import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.flag.types.IBlockRegistry;

public interface IMaterialBlock {
    Material getMaterial();

    IBlockRegistry getFlag();
}
