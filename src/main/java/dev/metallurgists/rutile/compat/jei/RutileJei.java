package dev.metallurgists.rutile.compat.jei;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.registry.RutileAPI;
import dev.metallurgists.rutile.api.registry.RutileRegistries;
import dev.metallurgists.rutile.compat.jei.custom.element.ElementIngredientHelper;
import dev.metallurgists.rutile.compat.jei.custom.element.ElementIngredientRenderer;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IColorHelper;
import mezz.jei.api.registration.IIngredientAliasRegistration;
import mezz.jei.api.registration.IModIngredientRegistration;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class RutileJei implements IModPlugin {
    private static final ResourceLocation ID = Rutile.asResource("jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerIngredients(IModIngredientRegistration registration) {
        IColorHelper colorHelper = registration.getColorHelper();
        registration.register(RutileJeiConstants.ELEMENT, RutileRegistries.ELEMENTS.values(), new ElementIngredientHelper(colorHelper), new ElementIngredientRenderer(16));
    }

    @Override
    public void registerIngredientAliases(IIngredientAliasRegistration registration) {
        registration.addAliases(
                RutileJeiConstants.ELEMENT,
                RutileRegistries.ELEMENTS.values(),
                "element");
    }
}
