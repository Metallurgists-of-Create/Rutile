package dev.metallurgists.rutile.compat.jei;

import dev.metallurgists.rutile.api.composition.element.Element;
import mezz.jei.api.ingredients.IIngredientType;

public class RutileJeiConstants {

    public static final IIngredientType<Element> ELEMENT = new IIngredientType<>() {

        @Override
        public Class<? extends Element> getIngredientClass() {
            return Element.class;
        }

        public String getUid() {
            return "rutile:element";
        }
    };
}
