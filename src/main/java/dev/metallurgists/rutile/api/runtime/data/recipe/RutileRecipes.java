package dev.metallurgists.rutile.api.runtime.data.recipe;

import dev.metallurgists.rutile.api.registry.PluginRegistry;
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
        PluginRegistry.getInstance().forEach((pl, cf) -> cf.getRuntimeRecipes().run(consumer));
    }

    public static void recipeRemoval(Consumer<ResourceLocation> registry) {
        final Consumer<ResourceLocation> actualConsumer = registry.andThen(RECIPE_FILTERS::add);
        RECIPE_FILTERS.clear();
        PluginRegistry.getInstance().forEach((pl, cf) -> cf.getRuntimeRecipeRemover().removals(actualConsumer));
    }
}
