package dev.metallurgists.rutile.api.material.module.registry.constructor;

import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.material.module.registry.RegistryModule;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;

@FunctionalInterface
public interface BlockConstructor {
    Block create(Block.Properties properties, Holder<RegistryModule.Key> registerKey, Material material);
}
