package dev.metallurgists.rutile.compat.jei;

import dev.metallurgists.rutile.api.element.ElementStack;
import mezz.jei.api.ingredients.IIngredientType;

public class RutileJeiConstants {
    public static final IIngredientType<ElementStack> ELEMENT = new IIngredientType<>() {

        @Override
        public Class<? extends ElementStack> getIngredientClass() {
            return ElementStack.class;
        }

        public String getUid() {
            return "rutile:element";
        }
    };
}
