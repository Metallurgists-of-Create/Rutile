package dev.metallurgists.rutile.compat.jei;

import dev.metallurgists.rutile.api.data.server.manager.composition.ItemCompositionManager;
import dev.metallurgists.rutile.api.element.Element;
import dev.metallurgists.rutile.api.element.ElementStack;
import dev.metallurgists.rutile.compat.jei.category.ElementCompositionWrapper;
import dev.metallurgists.rutile.registry.RutileRegistries;
import dev.metallurgists.rutile.util.GuiTexture;
import mezz.jei.api.ingredients.IIngredientType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.world.flag.FeatureFlagSet;

import java.util.*;

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

    public static final GuiTexture JEI_SLOT = new GuiTexture("jei/widgets", 18, 18);

    public static List<ElementStack> elementsList() {
        final List<ElementStack> elementList = new ArrayList<>();

        Minecraft minecraft = Minecraft.getInstance();
        FeatureFlagSet features = Optional.ofNullable(minecraft.player)
                .map(p -> p.connection)
                .map(ClientPacketListener::enabledFeatures)
                .orElse(FeatureFlagSet.of());

        ClientLevel level = minecraft.level;
        if (level == null) {
            throw new NullPointerException("minecraft.level must be set before JEI fetches ingredients");
        }

        for (Element element : RutileRegistries.ELEMENTS_REGISTRY) {
            if (element.isEnabled(features)) {
                elementList.add(element.asStack());
            }
        }

        return elementList;
    }

    @SuppressWarnings("rawtypes")
    public static List<ElementCompositionWrapper> getCompositionRecipes() {
        final List<ElementCompositionWrapper> compositionList = new ArrayList<>();
        for (var info : ItemCompositionManager.getInstance().getCompositions().entrySet()) {
            var recipe = new ElementCompositionWrapper.Items(info.getKey(), info.getValue());
            compositionList.add(recipe);
        }
        return compositionList;
    }
}
