package dev.metallurgists.rutile.registry;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.runtime.data.recipe.ExDataComponentIngredient;
import net.neoforged.neoforge.common.crafting.IngredientType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class RutileIngredientTypes {
    public static final DeferredRegister<IngredientType<?>> ITEM_INGREDIENT_TYPES = DeferredRegister
            .create(NeoForgeRegistries.INGREDIENT_TYPES, Rutile.ID);

    public static final DeferredHolder<IngredientType<?>, IngredientType<ExDataComponentIngredient>> DATA_COMPONENT_INGREDIENT = ITEM_INGREDIENT_TYPES
            .register("components", () -> new IngredientType<>(ExDataComponentIngredient.CODEC));
}
