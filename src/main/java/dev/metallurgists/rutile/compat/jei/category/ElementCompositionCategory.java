package dev.metallurgists.rutile.compat.jei.category;

import dev.metallurgists.rutile.compat.jei.RutileJeiConstants;
import dev.metallurgists.rutile.registry.RutileElements;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import javax.annotation.Nullable;

public class ElementCompositionCategory implements IRecipeCategory<ElementCompositionWrapper> {
    public static final RecipeType<ElementCompositionWrapper> RECIPE_TYPE = RecipeType.create("rutile", "element_composition", ElementCompositionWrapper.class);

    private final IDrawable icon;
    private final Component localizedName;

    public ElementCompositionCategory(IGuiHelper helper) {
        this.icon = helper.createDrawableIngredient(RutileJeiConstants.ELEMENT, RutileElements.O.asStack());
        this.localizedName = Component.translatable("gui.rutile.element_composition_jei");
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ElementCompositionWrapper recipe, IFocusGroup focuses) {
        recipe.setRecipe(builder, focuses);
    }

    @Override
    public RecipeType<ElementCompositionWrapper> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return localizedName;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    @SuppressWarnings("removal")
    public IDrawable getBackground() {
        return new EmptyBackground();
    }

    static class EmptyBackground implements IDrawable {
        @Override
        public int getWidth() {
            return 177;
        }

        @Override
        public int getHeight() {
            return 50;
        }

        @Override
        public void draw(GuiGraphics guiGraphics, int xOffset, int yOffset) {}
    }
}
