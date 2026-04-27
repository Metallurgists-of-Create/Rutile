package dev.metallurgists.rutile.api.material.module.registry.constructor;

import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.material.module.registry.RegistryModule;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;

@FunctionalInterface
public interface ItemConstructor {
    Item create(Item.Properties properties, Holder<RegistryModule.Key> registerKey, Material material);
}
