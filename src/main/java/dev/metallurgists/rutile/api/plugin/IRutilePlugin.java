package dev.metallurgists.rutile.api.plugin;

import com.tterrag.registrate.AbstractRegistrate;
import dev.metallurgists.rutile.api.dynamic_pack.data.recipe.handler.*;
import dev.metallurgists.rutile.api.material.flag.FlagKey;
import dev.metallurgists.rutile.api.registrate.RutileRegistrate;

import java.util.ArrayList;
import java.util.List;

public interface IRutilePlugin {

    String getPluginNamespace();

    List<FlagKey<?>> flagKeys = new ArrayList<>();

    default List<FlagKey<?>> getFlagKeys() {
        return flagKeys;
    }

    default IRutileRecipeHandler getRuntimeRecipes() { return new EmptyRecipeHandler(); };

    default IRutileMaterialRecipeHandler getRuntimeMaterialRecipes() { return  new EmptyMaterialRecipeHandler(); };

    default IRutileRecipeRemover getRuntimeRecipeRemover() { return new EmptyRecipeRemover(); };

    /**
        Register this in you mod container
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
