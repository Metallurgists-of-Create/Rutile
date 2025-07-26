package dev.metallurgists.rutile.api.material.flag.types;

import dev.metallurgists.rutile.api.material.base.Material;
import net.minecraft.data.recipes.FinishedRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public interface IRecipeHandler {
    void run(@NotNull Consumer<FinishedRecipe> provider, @NotNull Material material);
}
