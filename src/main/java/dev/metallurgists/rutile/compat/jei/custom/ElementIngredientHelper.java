package dev.metallurgists.rutile.compat.jei.custom;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.composition.element.ElementStack;
import dev.metallurgists.rutile.compat.jei.RutileJeiConstants;
import mezz.jei.api.helpers.IColorHelper;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;
import java.util.List;
import java.util.Optional;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ElementIngredientHelper implements IIngredientHelper<ElementStack> {

    private final IColorHelper colorHelper;

    public ElementIngredientHelper(IColorHelper colorHelper) {
        this.colorHelper = colorHelper;
    }

    @Override
    public IIngredientType<ElementStack> getIngredientType() {
        return RutileJeiConstants.ELEMENT;
    }

    @Override
    public String getDisplayName(ElementStack element) {
        return element.getElement().getDisplayName().getString();
    }

    @SuppressWarnings("removal")
    @Override
    public String getUniqueId(ElementStack element, UidContext context) {
        return getResourceLocation(element).toString();
    }

    @Override
    public String getUid(ElementStack element, UidContext context) {
        return getResourceLocation(element).toString();
    }

    @Override
    public ResourceLocation getResourceLocation(ElementStack element) {
        return element.getId();
    }

    @Override
    public String getErrorInfo(@Nullable ElementStack element) {
        if (element == null) {
            return "null";
        }
        return getResourceLocation(element).toString();
    }

    @Override
    public long getAmount(ElementStack elementStack) {
        return elementStack.getAmount();
    }

    @Override
    public ElementStack copyWithAmount(ElementStack elementStack, long amount) {
        ElementStack copy = elementStack.copy();
        int intAmount = Math.toIntExact(amount);
        copy.setAmount(intAmount);
        return copy;
    }

    @Override
    public ElementStack copyIngredient(ElementStack elementStack) {
        return elementStack.copy();
    }

    @Override
    public ElementStack normalizeIngredient(ElementStack elementStack) {
        if (elementStack.getAmount() == 1) {
            return elementStack;
        }
        int originalAmount = elementStack.getAmount();
        elementStack.setAmount(1);
        ElementStack copy = elementStack.copy();
        elementStack.setAmount(originalAmount);
        return copy;
    }
    @Override
    public Iterable<Integer> getColors(ElementStack element) {
        return getStillFluidSprite()
                .map(fluidStillSprite -> {
                    int renderColor = new Color(element.getElement().getColor(), true).getRGB();
                    return colorHelper.getColors(fluidStillSprite, renderColor, 1);
                })
                .orElseGet(List::of);
    }

    public Optional<TextureAtlasSprite> getStillFluidSprite() {
        return Optional.ofNullable(Minecraft.getInstance()
                        .getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
                        .apply(Rutile.id("fluid/thin_fluid_still")))
                .filter(s -> s.atlasLocation() != MissingTextureAtlasSprite.getLocation());
    }
}
