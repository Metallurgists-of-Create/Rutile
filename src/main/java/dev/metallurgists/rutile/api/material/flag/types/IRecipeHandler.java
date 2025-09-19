package dev.metallurgists.rutile.api.material.flag.types;

import dev.metallurgists.rutile.api.material.base.Material;
import net.minecraft.data.recipes.RecipeOutput;
import org.jetbrains.annotations.NotNull;

public interface IRecipeHandler {
    void run(@NotNull RecipeOutput output, @NotNull Material material);
}
