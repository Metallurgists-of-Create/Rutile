package dev.metallurgists.rutile.api.runtime.data.recipe.handler;

import net.minecraft.data.recipes.RecipeOutput;
import org.jetbrains.annotations.NotNull;

public interface IRutileRecipeHandler {
    void run(@NotNull RecipeOutput output);

    class Empty implements IRutileRecipeHandler {
        @Override
        public void run(@NotNull RecipeOutput output) {}
    }
}
