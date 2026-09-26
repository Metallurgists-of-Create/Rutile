package dev.metallurgists.rutile.compat.jei;

import dev.metallurgists.rutile.api.composition.Composition;
import dev.metallurgists.rutile.api.composition.element.Element;
import dev.metallurgists.rutile.api.composition.element.ElementStack;
import dev.metallurgists.rutile.api.data.manager.composition.FluidCompositionManager;
import dev.metallurgists.rutile.api.data.manager.composition.ItemCompositionManager;
import dev.metallurgists.rutile.compat.jei.category.ElementCompositionWrapper;
import dev.metallurgists.rutile.registry.RutileRegistries;
import dev.metallurgists.rutile.util.GuiTexture;
import mezz.jei.api.ingredients.IIngredientType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

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

        Minecraft minecraft = Minecraft.getInstance();

        ClientLevel level = minecraft.level;
        if (level == null) {
            throw new NullPointerException("minecraft.level must be set before JEI fetches ingredients");
        }

        return new ArrayList<>(RutileRegistries.ELEMENTS_REGISTRY.stream().map(Element::asStack).toList());
    }

    @SuppressWarnings("rawtypes")
    public static List<ElementCompositionWrapper> getCompositionRecipes() {
        final List<ElementCompositionWrapper> compositionList = new ArrayList<>();
        groupByComposition(ItemCompositionManager.getInstance().getCompositions())
                .forEach((composition, items) -> compositionList.add(new ElementCompositionWrapper.Items(items, composition)));
        groupByComposition(FluidCompositionManager.getInstance().getCompositions())
                .forEach((composition, fluids) -> compositionList.add(new ElementCompositionWrapper.Fluids(fluids, composition)));
        return compositionList;
    }

    private static <T> Map<Composition<T>, List<T>> groupByComposition(Map<T, Composition<T>> compositions) {
        Map<Composition<T>, List<T>> groups = new IdentityHashMap<>();
        for (Map.Entry<T, Composition<T>> entry : compositions.entrySet()) {
            groups.computeIfAbsent(entry.getValue(), c -> new ArrayList<>()).add(entry.getKey());
        }
        return groups;
    }
}
