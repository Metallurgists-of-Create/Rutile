package dev.metallurgists.rutile.api.dynamic_pack.data.recipe.handler;

import dev.metallurgists.rutile.api.material.base.Material;
import net.minecraft.data.recipes.RecipeOutput;
import org.jetbrains.annotations.NotNull;

public interface IRutileMaterialRecipeHandler {
    void run(@NotNull RecipeOutput output, @NotNull Material material);
}
