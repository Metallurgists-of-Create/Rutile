package dev.metallurgists.rutile.api.dynamic_pack.data.recipe;

import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.flag.types.IRecipeHandler;
import dev.metallurgists.rutile.api.plugin.RutilePluginFinder;
import dev.metallurgists.rutile.api.registry.RutileAPI;
import dev.metallurgists.rutile.api.registry.material.MaterialRegistry;
import dev.metallurgists.rutile.registry.RutileRegistries;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.neoforged.neoforge.common.conditions.ICondition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.function.Consumer;

public class RutileRecipes {
    public static final Set<ResourceLocation> RECIPE_FILTERS = new ObjectOpenHashSet<>();

    public static void recipeAddition(RecipeOutput originalConsumer) {
        RecipeOutput consumer = new RecipeOutput() {
            @Override
            public Advancement.@NotNull Builder advancement() {
                return originalConsumer.advancement();
            }
            @Override
            public void accept(@NotNull ResourceLocation id, @NotNull Recipe<?> recipe, @Nullable AdvancementHolder advancement, ICondition @NotNull... conditions) {
                if (!RECIPE_FILTERS.contains(id)) {
                    originalConsumer.accept(id, recipe, advancement, conditions);
                }
            }
        };


        for (Material material : RutileRegistries.MATERIAL_REGISTRY) {
            for (var flagKey : RutileRegistries.FLAG_KEY_REGISTRY) {
                if (material.hasFlag(flagKey) && material.getFlag(flagKey) instanceof IRecipeHandler handler) {
                    handler.run(consumer, material);
                }
            }
            RutilePluginFinder.getModPlugins().forEach(p -> p.getRuntimeMaterialRecipes().run(consumer, material));
        }
        RutilePluginFinder.getModPlugins().forEach(p -> p.getRuntimeRecipes().run(consumer));
    }

    public static void recipeRemoval(Consumer<ResourceLocation> registry) {
        final Consumer<ResourceLocation> actualConsumer = registry.andThen(RECIPE_FILTERS::add);
        RECIPE_FILTERS.clear();

        RutileRecipeRemoval.init(actualConsumer);
        RutilePluginFinder.getModPlugins().forEach(p -> p.getRuntimeRecipeRemover().removals(actualConsumer));
    }
}
