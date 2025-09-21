package dev.metallurgists.rutile.api.dynamic_pack.data.recipe.handler;

import dev.metallurgists.rutile.api.dynamic_pack.data.recipe.RutileRecipes;
import net.minecraft.data.recipes.RecipeOutput;
import org.jetbrains.annotations.NotNull;

/**
 * Empty Recipe Handler class for plugins that don't need them.
 * @see RutileRecipes
 */
public class EmptyRecipeHandler implements IRutileRecipeHandler {
    @Override
    public void run(@NotNull RecipeOutput output) {

    }
}
