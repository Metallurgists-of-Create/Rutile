package dev.metallurgists.rutile.api.composition;

import dev.metallurgists.rutile.RutileClient;
import dev.metallurgists.rutile.api.composition.element.ElementStack;
import dev.metallurgists.rutile.api.data.manager.composition.FluidCompositionManager;
import dev.metallurgists.rutile.api.data.manager.composition.ItemCompositionManager;
import dev.metallurgists.rutile.config.RutileConfig;
import dev.metallurgists.rutile.util.StringFormatUtil;
import net.createmod.catnip.utility.lang.LangBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.function.Consumer;

public class CompositionHandler {

    public static void appendItemTooltips(List<Component> toolTip, ItemStack stack, HolderLookup.Provider registries) {
        boolean hasSpecificComposition = itemComposition(toolTip, stack);
        //TODO: Material compositions
        if (!hasSpecificComposition) {
            var fluidHandler = stack.getCapability(Capabilities.FluidHandler.ITEM);
            if (fluidHandler != null && !fluidHandler.getFluidInTank(0).isEmpty()) {
                appendFluidTooltips(fluidHandler.getFluidInTank(0), toolTip::add);
            }
        }
    }

    public static void appendFluidTooltips(FluidStack fluidStack, Consumer<Component> tooltips) {
        fluidComposition(tooltips, fluidStack);
    }

    public static boolean itemComposition(List<Component> toolTip, ItemStack stack) {
        Composition<?> composition = ItemCompositionManager.getInstance().getComposition(stack.getItem());
        if (composition != null) {
            LangBuilder compositionName = RutileClient.getLang();
            createTooltip(compositionName, composition);
            add(toolTip, compositionName);
            return true;
        }
        return false;
    }

    public static void fluidComposition(Consumer<Component> toolTip, FluidStack stack) {
        Composition<?> composition = FluidCompositionManager.getInstance().getComposition(convertToStill(stack.getFluid()));
        if (composition != null) {
            LangBuilder compositionName = RutileClient.getLang();
            createTooltip(compositionName, composition);
            add(toolTip, compositionName);
        }
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

    public static void createTooltip(LangBuilder compositionName, Composition<?> composition) {
        List<SubComposition> compositions = composition.compositions();
        int defaultColor = RutileConfig.getClient().tooltipColor.get();

        for (int i = 0; i < compositions.size(); i++) {
            if (i > 0) {
                compositionName.add(RutileClient.getLang().text(" + ").color(defaultColor));
            }
            appendSubComposition(compositionName, compositions.get(i));
        }
    }

    @SuppressWarnings("null")
    private static void appendSubComposition(LangBuilder builder, SubComposition subComposition) {
        if (subComposition == null) return;

        boolean useElementColor = RutileConfig.getClient().elementColorForTooltip.get();
        int defaultColor = RutileConfig.getClient().tooltipColor.get();

        if (subComposition.getAmount() > 1) {
            int rootColor = useElementColor ? subComposition.getColor() : defaultColor;
            builder.add(RutileClient.getLang().text(String.valueOf(subComposition.getAmount())).color(rootColor));
        }

        Deque<DisplayFrame> stack = new ArrayDeque<>();
        stack.push(new DisplayFrame(subComposition));

        while (!stack.isEmpty()) {
            DisplayFrame frame = stack.peek();

            if (!frame.elementsProcessed) {
                for (ElementStack stackItem : frame.node.getElements()) {
                    String text = "";

                    if (stackItem.getMass() != stackItem.getElement().getMass()) {
                        text += StringFormatUtil.toUpperNumbers(Math.round(stackItem.getMass()));
                    }

                    text += stackItem.getElement().getSymbol();

                    if (stackItem.getAmount() > 1) {
                        text += stackItem.getAmount();
                    }

                    if (useElementColor) {
                        int color = stackItem.getElement().getColor();
                        builder.add(RutileClient.getLang().text(text).color(color));
                    } else {
                        builder.add(RutileClient.getLang().text(text).color(defaultColor));
                    }
                }
                frame.elementsProcessed = true;
            }

            if (frame.childIndex < frame.node.getNested().size()) {
                SubComposition child = frame.node.getNested().get(frame.childIndex);
                frame.childIndex++;

                if (child.isHydrate()) {
                    builder.add(RutileClient.getLang().text(" ⋅ ").color(defaultColor));
                    if (child.getAmount() > 1) {
                        int hydrateColor = useElementColor ? child.getColor() : defaultColor;
                        builder.add(RutileClient.getLang().text(String.valueOf(child.getAmount())).color(hydrateColor));
                    }
                } else {
                    int bracketColor = useElementColor ? child.getColor() : defaultColor;
                    builder.add(RutileClient.getLang().text("(").color(bracketColor));
                }

                stack.push(new DisplayFrame(child));
            } else {
                stack.pop();

                if (!stack.isEmpty()) {
                    if (!frame.node.isHydrate()) {
                        int bracketColor = useElementColor ? frame.node.getColor() : defaultColor;
                        builder.add(RutileClient.getLang().text(")").color(bracketColor));
                        if (frame.node.getAmount() > 1) {
                            builder.add(RutileClient.getLang().text(String.valueOf(frame.node.getAmount())).color(bracketColor));
                        }
                    }
                }
            }
        }
    }

    private static class DisplayFrame {
        final SubComposition node;
        int childIndex = 0;
        boolean elementsProcessed = false;

        DisplayFrame(SubComposition node) {
            this.node = node;
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