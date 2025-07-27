package dev.metallurgists.rutile.common;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.material.base.Material;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class RecipeHelper {

    public static void craftCompact(@NotNull Consumer<FinishedRecipe> provider, ItemLike input, ItemLike output, boolean isSmall, Material material, String recipeId) {
        ShapedRecipeBuilder builder = new ShapedRecipeBuilder(RecipeCategory.MISC, output, 1);
        if (isSmall) {
            for (int i = 0; i < 2; i++) {
                builder.pattern("##");
            }
        } else {
            for (int i = 0; i < 3; i++) {
                builder.pattern("###");
            }
        }
        builder.define('#', input)
                .unlockedBy("has_input", InventoryChangeTrigger.TriggerInstance.hasItems(input))
                .save(provider, Rutile.asResource("runtime_generated/" + material.getNamespace() + "/" + recipeId.formatted(material.getName())));
    }

    public static void craftCompact(@NotNull Consumer<FinishedRecipe> provider, ItemLike input, ItemLike output, int amountIn, Material material, String recipeId) {
        ShapelessRecipeBuilder builder = new ShapelessRecipeBuilder(RecipeCategory.MISC, output, 1);
        for (int i = 0; i < amountIn; i++) {
            builder.requires(input);
        }
        builder.unlockedBy("has_input", InventoryChangeTrigger.TriggerInstance.hasItems(input))
                .save(provider, Rutile.asResource("runtime_generated/" + material.getNamespace() + "/" + recipeId.formatted(material.getName())));
    }

    public static void craftDecompact(@NotNull Consumer<FinishedRecipe> provider, ItemLike input, ItemLike output, int amountOut, Material material, String recipeId) {
        ShapelessRecipeBuilder builder = new ShapelessRecipeBuilder(RecipeCategory.MISC, output, amountOut);
        builder.requires(input)
                .unlockedBy("has_input", InventoryChangeTrigger.TriggerInstance.hasItems(input))
                .save(provider, Rutile.asResource("runtime_generated/" + material.getNamespace() + "/" + recipeId.formatted(material.getName())));
    }
}
