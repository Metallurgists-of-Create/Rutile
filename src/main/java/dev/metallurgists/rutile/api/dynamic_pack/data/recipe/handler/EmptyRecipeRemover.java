package dev.metallurgists.rutile.api.dynamic_pack.data.recipe.handler;

import dev.metallurgists.rutile.api.dynamic_pack.data.recipe.RutileRecipes;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

/**
 * Empty Recipe Removal class for plugins that don't need them.
 * @see RutileRecipes
 */
public class EmptyRecipeRemover implements IRutileRecipeRemover {
    @Override
    public void removals(Consumer<ResourceLocation> registry) {

    }
}
