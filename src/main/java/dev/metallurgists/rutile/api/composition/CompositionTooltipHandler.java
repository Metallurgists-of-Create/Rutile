package dev.metallurgists.rutile.api.composition;

import dev.metallurgists.rutile.api.composition.tooltip.CompositionManager;
import dev.metallurgists.rutile.api.composition.tooltip.MaterialCompositionManager;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.registry.RutileAPI;
import dev.metallurgists.rutile.api.registry.material.MaterialRegistry;
import dev.metallurgists.rutile.config.RutileConfig;
import dev.metallurgists.rutile.util.ClientUtil;
import dev.metallurgists.rutile.util.helpers.MaterialHelpers;
import net.createmod.catnip.lang.LangBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Objects;

public class CompositionTooltipHandler {

    public static void addToTooltip(List<Component> toolTip, ItemStack stack) {
        boolean notAMaterialCheckSingleCompositions = false;
        for (MaterialRegistry registry : RutileAPI.materialManager.getRegistries()) {
            for (Material material : registry.getAllMaterials()) {
                if (MaterialCompositionManager.hasComposition(material)) {
                    var allMatItem = MaterialHelpers.getAllMaterialItemsForTooltips(material);
                    if (allMatItem.contains(stack.getItem())) {
                        LangBuilder compositionName = ClientUtil.lang();
                        createTooltip(compositionName, MaterialCompositionManager.getSubCompositions(material));
                        add(toolTip, compositionName);
                        break;
                    } else {
                        notAMaterialCheckSingleCompositions = true;
                    }
                }
            }
        }
        if (notAMaterialCheckSingleCompositions) {
            if (CompositionManager.hasComposition(stack.getItem())) {
                LangBuilder compositionName = ClientUtil.lang();
                createTooltip(compositionName, CompositionManager.getSubCompositions(stack.getItem()));
                add(toolTip, compositionName);
            }
        }
    }

    private static void add(List<Component> toolTip, LangBuilder composition) {
        if (!composition.string().isEmpty()) {
            Component component = ClientUtil.lang().space().space().space()
                    .add(composition)
                    .component().withStyle(style -> style.withColor(RutileConfig.client().tooltipColor.get()));
            if (toolTip.size() < 2)
                toolTip.add(component);
            else
                toolTip.add(1, component);
        }
    }

    private static void createTooltip(LangBuilder compositionName, List<SubComposition> subCompositions) {
        for (SubComposition composition : subCompositions) {
            if (composition == null) continue;
            LangBuilder subComp = ClientUtil.lang();
            SubComposition subComposition = Objects.requireNonNull(composition);
            int elementsSize = subComposition.getElements().size();
            if (elementsSize > 1) {
                subComp.add(Component.literal("("));
                for (int j = 0; j < elementsSize; j++) {
                    if (subComposition.getElements().get(j) == null) continue;
                    subComp.add(Component.literal(subComposition.getElement(j).getDisplay()));
                }
                subComp.add(Component.literal(")"));
            } else {
                subComp.add(Component.literal(subComposition.getElement(0).getDisplay()));
            }
            compositionName.add(subComp);
        }
    }
}
