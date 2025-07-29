package dev.metallurgists.rutile.api.plugin;

import com.tterrag.registrate.AbstractRegistrate;
import dev.metallurgists.rutile.api.dynamic_pack.data.recipe.handler.*;
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
    default AbstractRegistrate<?> getRegistrate() {
        return RutileRegistrate.create(getPluginNamespace());
    }

    default void registerElements() {

    }

    default void registerFlags() {

    }

    default void modifyMaterials() {

    }

    default void registerMaterials() {

    }
}
