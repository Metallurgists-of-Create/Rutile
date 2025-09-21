package dev.metallurgists.rutile.compat.jei;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.composition.element.Element;
import dev.metallurgists.rutile.api.registry.CustomRutileRegistries;
import dev.metallurgists.rutile.compat.jei.custom.element.ElementIngredientHelper;
import dev.metallurgists.rutile.compat.jei.custom.element.ElementIngredientRenderer;
import dev.metallurgists.rutile.registry.RutileRegistries;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IColorHelper;
import mezz.jei.api.registration.IIngredientAliasRegistration;
import mezz.jei.api.registration.IModIngredientRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class RutileJei implements IModPlugin {
    private static final ResourceLocation ID = Rutile.id("jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerIngredients(IModIngredientRegistration registration) {
        IColorHelper colorHelper = registration.getColorHelper();
        registration.register(RutileJeiConstants.ELEMENT, CustomRutileRegistries.ELEMENTS.stream().toList(), new ElementIngredientHelper(colorHelper), new ElementIngredientRenderer(16), Element.DIRECT_CODEC);
    }

    @Override
    public void registerIngredientAliases(IIngredientAliasRegistration registration) {
        registration.addAliases(
                RutileJeiConstants.ELEMENT,
                CustomRutileRegistries.ELEMENTS.stream().toList(),
                "element");
    }
}
