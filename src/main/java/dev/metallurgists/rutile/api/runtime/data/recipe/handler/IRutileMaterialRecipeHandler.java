package dev.metallurgists.rutile.api.runtime.data.recipe.handler;

import dev.metallurgists.rutile.api.material.Material;
import net.minecraft.data.recipes.RecipeOutput;
import org.jetbrains.annotations.NotNull;

public interface IRutileMaterialRecipeHandler {
    void run(@NotNull RecipeOutput output, @NotNull Material material);

    class Empty implements IRutileMaterialRecipeHandler {
        @Override
        public void run(@NotNull RecipeOutput output, @NotNull Material material) {}
    }
}
