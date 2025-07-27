package dev.metallurgists.rutile.api.dynamic_pack.data.recipe;

import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.flag.types.IRecipeHandler;
import dev.metallurgists.rutile.api.plugin.RutilePluginFinder;
import dev.metallurgists.rutile.api.registry.RutileAPI;
import dev.metallurgists.rutile.api.registry.material.MaterialRegistry;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;

import java.util.Set;
import java.util.function.Consumer;

public class RutileRecipes {
    public static final Set<ResourceLocation> RECIPE_FILTERS = new ObjectOpenHashSet<>();

    public static void recipeAddition(Consumer<FinishedRecipe> originalConsumer) {
        Consumer<FinishedRecipe> consumer = recipe -> {
            if (!RECIPE_FILTERS.contains(recipe.getId())) {
                originalConsumer.accept(recipe);
            }
        };

        for (var flagKey : RutileAPI.getRegisteredFlags().values()) {
            for (MaterialRegistry registry : RutileAPI.materialManager.getRegistries()) {
                for (Material material : registry.getAllMaterials()) {
                    if (material.hasFlag(flagKey) && material.getFlag(flagKey) instanceof IRecipeHandler handler) {
                        handler.run(consumer, material);
                    }
                    RutilePluginFinder.getModPlugins().forEach(p -> p.getRuntimeMaterialRecipes().run(consumer, material));
                }
            }
        }
        RutilePluginFinder.getModPlugins().forEach(p -> p.getRuntimeRecipes().run(consumer));
    }

    public static void recipeRemoval() {
        RECIPE_FILTERS.clear();
        RutileRecipeRemoval.init(RECIPE_FILTERS::add);
        RutilePluginFinder.getModPlugins().forEach(p -> p.getRuntimeRecipeRemover().removals(RECIPE_FILTERS::add));
    }
}
