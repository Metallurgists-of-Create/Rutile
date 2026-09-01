package dev.metallurgists.rutile.api.composition;

import dev.metallurgists.rutile.RutileClient;
import dev.metallurgists.rutile.api.composition.element.ElementStack;
import dev.metallurgists.rutile.api.data.manager.composition.FluidCompositionManager;
import dev.metallurgists.rutile.api.data.manager.composition.ItemCompositionManager;
import dev.metallurgists.rutile.config.RutileConfig;
import dev.metallurgists.rutile.util.ColourUtil;
import dev.metallurgists.rutile.util.StringFormatUtil;
import net.createmod.catnip.lang.LangBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;
import java.util.function.Consumer;

public class CompositionHandler {

    public static void appendItemTooltips(List<Component> toolTip, ItemStack stack, HolderLookup.Provider registries) {
        boolean hasSpecificComposition = itemComposition(toolTip, stack);
        //TODO: Material compositions
        if (!hasSpecificComposition) {
            //materialComposition(toolTip, stack);
        }
    }

    public static void appendFluidTooltips(FluidStack fluidStack, Consumer<Component> tooltips) {
        Fluid fluid = fluidStack.getFluid();
        boolean hasSpecificComposition = fluidComposition(tooltips, fluidStack);
    }

    public static boolean itemComposition(List<Component> toolTip, ItemStack stack) {
        Composition composition = ItemCompositionManager.getInstance().getComposition(stack.getItem());
        if (composition != null) {
            LangBuilder compositionName = RutileClient.getLang();
            createTooltip(compositionName, composition);
            add(toolTip, compositionName);
            return true;
        }
        return false;
    }

    public static boolean fluidComposition(Consumer<Component> toolTip, FluidStack stack) {
        Composition composition = FluidCompositionManager.getInstance().getComposition(convertToStill(stack.getFluid()));
        if (composition != null) {
            LangBuilder compositionName = RutileClient.getLang();
            createTooltip(compositionName, composition);
            add(toolTip, compositionName);
            return true;
        }
        return false;
    }

    private static void add(List<Component> toolTip, LangBuilder composition) {
        if (!composition.string().isEmpty()) {
            MutableComponent component = RutileClient.getLang().space().space().space()
                    .add(composition)
                    .component();
            if (!RutileConfig.getClient().elementColorForTooltip.get()) {
                component = component.withStyle(style -> style.withColor(RutileConfig.getClient().tooltipColor.get()));
            }
            if (toolTip.size() < 2)
                toolTip.add(component);
            else
                toolTip.add(1, component);
        }
    }

    private static void add(Consumer<Component> toolTip, LangBuilder composition) {
        if (!composition.string().isEmpty()) {
            MutableComponent component = RutileClient.getLang().space().space().space()
                    .add(composition)
                    .component();
            if (!RutileConfig.getClient().elementColorForTooltip.get()) {
                component = component.withStyle(style -> style.withColor(RutileConfig.getClient().tooltipColor.get()));
            }
            toolTip.accept(component);
        }
    }

    public static void createTooltip(LangBuilder compositionName, Composition composition) {
        for (SubComposition subComposition : composition.compositions()) {
            if (subComposition == null) continue;
            LangBuilder subComp = RutileClient.getLang();
            int subCompAmount = composition.compositions().size();
            boolean encaseInBrackets = subCompAmount > 1;
            int outerColour = ColourUtil.blendAll(subComposition.getElements().stream().map(ElementStack::getColor).toList());
            var outerStyle = Style.EMPTY.withColor(outerColour);
            if (!RutileConfig.getClient().elementColorForTooltip.get()) {
                outerStyle = Style.EMPTY.withColor(RutileConfig.getClient().tooltipColor.get());
            }
            if (encaseInBrackets) subComp.add(Component.literal("(").setStyle(outerStyle));
            for (int j = 0; j < subComposition.getElements().size(); j++) {
                if (subComposition.getElements().get(j) == null) continue;
                ElementStack elementStack = subComposition.getElements().get(j);
                MutableComponent elementComp = Component.literal(elementStack.getDisplay());
                if (RutileConfig.getClient().elementColorForTooltip.get()) {
                    elementComp = elementComp.setStyle(Style.EMPTY.withColor(elementStack.getColor()));
                }
                subComp.add(elementComp);
            }
            if (encaseInBrackets) subComp.add(Component.literal(")").setStyle(outerStyle));
            if (subComposition.getAmount() > 1) subComp.add(Component.literal(StringFormatUtil.toSmallDownNumbers(String.valueOf(subComposition.getAmount()))).setStyle(outerStyle));
            compositionName.add(subComp);
        }
    }

    public static Fluid convertToStill(Fluid fluid) {
        if (fluid == Fluids.FLOWING_WATER)
            return Fluids.WATER;
        if (fluid == Fluids.FLOWING_LAVA)
            return Fluids.LAVA;
        if (fluid instanceof BaseFlowingFluid)
            return ((BaseFlowingFluid) fluid).getSource();
        return fluid;
    }
}
