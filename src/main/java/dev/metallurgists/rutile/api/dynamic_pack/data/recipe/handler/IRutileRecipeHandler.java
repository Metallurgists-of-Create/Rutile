package dev.metallurgists.rutile.api.dynamic_pack.data.recipe.handler;

import net.minecraft.data.recipes.RecipeOutput;
import org.jetbrains.annotations.NotNull;

public interface IRutileRecipeHandler {
    void run(@NotNull RecipeOutput output);
}
