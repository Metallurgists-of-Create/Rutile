package dev.metallurgists.rutile.compat.jei;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.element.ElementStack;
import dev.metallurgists.rutile.compat.jei.category.ElementCompositionCategory;
import dev.metallurgists.rutile.compat.jei.custom.ElementIngredientHelper;
import dev.metallurgists.rutile.compat.jei.custom.ElementIngredientRenderer;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IColorHelper;
import mezz.jei.api.registration.IIngredientAliasRegistration;
import mezz.jei.api.registration.IModIngredientRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class RutileJei implements IModPlugin {
    private static final ResourceLocation ID = Rutile.getResource("jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerIngredients(IModIngredientRegistration registration) {
        IColorHelper colorHelper = registration.getColorHelper();
        registration.register(RutileJeiConstants.ELEMENT, RutileJeiConstants.elementsList(), new ElementIngredientHelper(colorHelper), new ElementIngredientRenderer(16), ElementStack.CODEC);
    }

    @Override
    public void registerIngredientAliases(IIngredientAliasRegistration registration) {
        RutileJeiConstants.elementsList().forEach(element -> registration.addAlias(RutileJeiConstants.ELEMENT, element, element.getElement().getSymbol()));
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new ElementCompositionCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(ElementCompositionCategory.RECIPE_TYPE, RutileJeiConstants.getCompositionRecipes());
    }
}
