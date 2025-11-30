package dev.metallurgists.rutile.compat.jei.custom.element;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.composition.element.ElementStack;
import dev.metallurgists.rutile.compat.jei.RutileJeiConstants;
import mezz.jei.api.helpers.IColorHelper;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.createmod.catnip.theme.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

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
        return element.id();
    }

    @Override
    public ElementStack copyIngredient(ElementStack element) {
        return element;
    }

    @Override
    public String getErrorInfo(@Nullable ElementStack element) {
        if (element == null) {
            return "null";
        }
        return getResourceLocation(element).toString();
    }

    @Override
    public Iterable<Integer> getColors(ElementStack element) {
        return getStillFluidSprite()
                .map(fluidStillSprite -> {
                    int renderColor = new Color(element.getElement().color(), true).getRGB();
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
