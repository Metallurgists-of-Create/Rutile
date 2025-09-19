package dev.metallurgists.rutile.api.dynamic_pack.data.recipe.handler;

import dev.metallurgists.rutile.api.dynamic_pack.data.recipe.RutileRecipes;
import dev.metallurgists.rutile.api.material.base.Material;
import net.minecraft.data.recipes.RecipeOutput;
import org.jetbrains.annotations.NotNull;

/**
 * Empty Material Recipe Handler class for plugins that don't need them.
 * @see RutileRecipes
 */
public class EmptyMaterialRecipeHandler implements IRutileMaterialRecipeHandler {
    @Override
    public void run(@NotNull RecipeOutput output, @NotNull Material material) {

    }
}
