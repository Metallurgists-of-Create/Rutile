package dev.metallurgists.rutile.api.dynamic_pack.data.recipe.handler;

import dev.metallurgists.rutile.api.dynamic_pack.data.recipe.RutileRecipes;
import dev.metallurgists.rutile.api.material.base.Material;
import net.minecraft.data.recipes.FinishedRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Empty Material Recipe Handler class for plugins that don't need them.
 * @see RutileRecipes
 */
public class EmptyMaterialRecipeHandler implements IRutileMaterialRecipeHandler {
    @Override
    public void run(@NotNull Consumer<FinishedRecipe> provider, @NotNull Material material) {

    }
}
