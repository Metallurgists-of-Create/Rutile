package dev.metallurgists.rutile.api.composition;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.composition.tooltip.CompositionManager;
import dev.metallurgists.rutile.api.composition.tooltip.MaterialCompositionManager;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.registry.RutileAPI;
import dev.metallurgists.rutile.config.RutileConfig;
import dev.metallurgists.rutile.util.ClientUtil;
import dev.metallurgists.rutile.util.MaterialHelper;
import net.createmod.catnip.lang.LangBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class CompositionTooltipHandler {

    public static void addToTooltip(List<Component> toolTip, ItemStack stack) {
        boolean notAMaterialCheckSingleCompositions = false;
        for (Material material : RutileAPI.getRegisteredMaterials().values()) {
            if (MaterialCompositionManager.hasComposition(material)) {
                var allMatItem = MaterialHelper.getAllMaterialItemsForTooltips(material);
                if (allMatItem.contains(stack.getItem())) {
                    LangBuilder compositionName = ClientUtil.lang();
                    createTooltip(compositionName, MaterialCompositionManager.getSubCompositions(material));
                    if (!compositionName.string().isEmpty()) {
                        toolTip.add(ClientUtil.lang().space().space().space()
                                .add(compositionName)
                                .component().withStyle(style -> style.withColor(RutileConfig.client().tooltipColor.get())));
                    }
                    return; // No need to check further if we found a material match
                } else {
                    notAMaterialCheckSingleCompositions = true;
                }
            }
        }
        if (notAMaterialCheckSingleCompositions) {
            Rutile.LOGGER.info("Item: {} does not belong to a material. Checking single compositions", stack.getItem());
            if (CompositionManager.hasComposition(stack.getItem())) {
                LangBuilder compositionName = ClientUtil.lang();
                createTooltip(compositionName, CompositionManager.getSubCompositions(stack.getItem()));
                if (!compositionName.string().isEmpty()) {
                    toolTip.add(ClientUtil.lang().space().space().space()
                            .add(compositionName)
                            .component().withStyle(style -> style.withColor(RutileConfig.client().tooltipColor.get())));
                }
            }
        }
    }

    static Predicate<ItemStack> isMaterialItem() {
        return stack -> {
            for (Material material : RutileAPI.getRegisteredMaterials().values()) {
                if (MaterialCompositionManager.hasComposition(material)) {
                    var allMatItem = MaterialHelper.getAllMaterialItemsForTooltips(material);
                    return allMatItem.contains(stack.getItem());
                }
            }
            return false;
        };
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
