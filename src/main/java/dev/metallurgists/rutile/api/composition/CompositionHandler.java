package dev.metallurgists.rutile.api.composition;

import dev.metallurgists.rutile.RutileClient;
import dev.metallurgists.rutile.api.RutileApi;
import dev.metallurgists.rutile.api.data.manager.composition.ItemCompositionManager;
import dev.metallurgists.rutile.api.data.manager.composition.MaterialCompositionManager;
import dev.metallurgists.rutile.api.element.ElementStack;
import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.material.MaterialHelper;
import dev.metallurgists.rutile.api.material.stack.MaterialEntry;
import dev.metallurgists.rutile.api.tag.TagPrefix;
import dev.metallurgists.rutile.config.RutileConfig;
import net.createmod.catnip.lang.LangBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.List;

public class CompositionHandler {

    public static void addToTooltip(List<Component> toolTip, ItemStack stack, HolderLookup.Provider registries) {
        boolean hasSpecificComposition = itemComposition(toolTip, stack);
        if (!hasSpecificComposition) {
            materialComposition(toolTip, stack);
        }
    }

    public static boolean materialComposition(List<Component> toolTip, ItemStack stack) {
        for (TagPrefix tagPrefix : RutileApi.getTagPrefixRegistry().getAll()) {
            for (Material material : RutileApi.getMaterialRegistry().getAll()) {
                MaterialEntry materialEntry = new MaterialEntry(tagPrefix, material);
                List<Item> items = MaterialHelper.getItems(materialEntry).stream().map(ItemLike::asItem).toList();
                if (items.contains(stack.getItem())) {
                    Composition composition = MaterialCompositionManager.getInstance().getComposition(material);
                    if (composition != null) {
                        LangBuilder compositionName = RutileClient.lang();
                        createTooltip(compositionName, composition);
                        add(toolTip, compositionName);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean itemComposition(List<Component> toolTip, ItemStack stack) {
        Composition composition = ItemCompositionManager.getInstance().getComposition(stack.getItem());
        if (composition != null) {
            LangBuilder compositionName = RutileClient.lang();
            createTooltip(compositionName, composition);
            add(toolTip, compositionName);
            return true;
        }
        return false;
    }

    private static void add(List<Component> toolTip, LangBuilder composition) {
        if (!composition.string().isEmpty()) {
            MutableComponent component = RutileClient.lang().space().space().space()
                    .add(composition)
                    .component();
            if (!RutileConfig.client().elementColorForTooltip.get()) {
                component = component.withStyle(style -> style.withColor(RutileConfig.client().tooltipColor.get()));
            }
            if (toolTip.size() < 2)
                toolTip.add(component);
            else
                toolTip.add(1, component);
        }
    }

    public static void createTooltip(LangBuilder compositionName, Composition composition) {
        for (SubComposition subComposition : composition.compositions()) {
            if (subComposition == null) continue;
            LangBuilder subComp = RutileClient.lang();
            int subCompAmount = composition.compositions().size();
            boolean encaseInBrackets = subCompAmount > 1;
            if (encaseInBrackets) subComp.add(Component.literal("("));
            for (int j = 0; j < subComposition.getElements().size(); j++) {
                if (subComposition.getElements().get(j) == null) continue;
                ElementStack elementStack = subComposition.getElements().get(j);
                MutableComponent elementComp = Component.literal(elementStack.getDisplay());
                if (RutileConfig.client().elementColorForTooltip.get()) {
                    elementComp = elementComp.setStyle(Style.EMPTY.withColor(elementStack.getElement().getColor()));
                }
                subComp.add(elementComp);
            }
            if (encaseInBrackets) subComp.add(Component.literal(")"));
            compositionName.add(subComp);
        }
    }
}
