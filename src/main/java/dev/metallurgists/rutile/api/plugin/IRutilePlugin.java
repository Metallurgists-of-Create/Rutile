package dev.metallurgists.rutile.api.plugin;

import dev.metallurgists.rutile.api.dynamic_pack.data.recipe.handler.*;
import dev.metallurgists.rutile.api.material.registry.block.RutileMaterialBlocks;
import dev.metallurgists.rutile.api.material.registry.fluid.RutileMaterialFluids;
import dev.metallurgists.rutile.api.material.registry.item.RutileMaterialItems;
import dev.metallurgists.rutile.api.registrate.RutileRegistrate;

public interface IRutilePlugin {

    String getPluginNamespace();

    default IRutileRecipeHandler getRuntimeRecipes() { return new EmptyRecipeHandler(); };

    default IRutileMaterialRecipeHandler getRuntimeMaterialRecipes() { return  new EmptyMaterialRecipeHandler(); };

    default IRutileRecipeRemover getRuntimeRecipeRemover() { return new EmptyRecipeRemover(); };

    /**
        Register this in you mod container
        @see dev.metallurgists.rutile.api.registrate.RutileRegistrate#registerEventListeners(net.minecraftforge.eventbus.api.IEventBus)
     */
    default RutileRegistrate getRegistrate() {
        return RutileRegistrate.create(getPluginNamespace());
    }

    default void registerElements() {

    }

    default void registerFlags() {

    }
}
