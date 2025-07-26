package dev.metallurgists.rutile.api.dynamic_pack.data.recipe;

import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.flag.FlagKey;
import dev.metallurgists.rutile.api.material.flag.types.IRecipeHandler;
import dev.metallurgists.rutile.api.plugin.IRutilePlugin;
import dev.metallurgists.rutile.api.plugin.RutilePluginFinder;
import dev.metallurgists.rutile.api.registry.RutileAPI;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
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
        List<IRutilePlugin> plugins = RutilePluginFinder.getModPlugins();

        for (Material material : RutileAPI.getRegisteredMaterials().values()) {
            for (FlagKey<?> flagKey : material.getFlags().getFlagKeys()) {
                var flag = material.getFlag(flagKey);
                if (flag instanceof IRecipeHandler handler) {
                    handler.run(consumer, material);
                }
            }
            plugins.forEach(p -> p.getRuntimeMaterialRecipes().run(consumer, material));
        }
        plugins.forEach(p -> p.getRuntimeRecipes().run(consumer));
    }

    public static void recipeRemoval() {
        RECIPE_FILTERS.clear();
        RutileRecipeRemoval.init(RECIPE_FILTERS::add);
        RutilePluginFinder.getModPlugins().forEach(p -> p.getRuntimeRecipeRemover().removals(RECIPE_FILTERS::add));
    }
}
