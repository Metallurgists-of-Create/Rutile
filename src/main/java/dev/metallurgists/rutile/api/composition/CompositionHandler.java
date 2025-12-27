package dev.metallurgists.rutile.api.composition;

import dev.metallurgists.rutile.RutileClient;
import dev.metallurgists.rutile.api.RutileApi;
import dev.metallurgists.rutile.api.data.server.manager.composition.ItemCompositionManager;
import dev.metallurgists.rutile.api.data.server.manager.composition.MaterialCompositionManager;
import dev.metallurgists.rutile.api.element.ElementStack;
import dev.metallurgists.rutile.api.fluid.MaterialFluid;
import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.material.MaterialHelper;
import dev.metallurgists.rutile.api.material.stack.MaterialEntry;
import dev.metallurgists.rutile.api.registry.RutileRegistries;
import dev.metallurgists.rutile.api.tag.TagPrefix;
import dev.metallurgists.rutile.config.RutileConfig;
import dev.metallurgists.rutile.util.ColourUtil;
import net.createmod.catnip.lang.LangBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.EmptyFluid;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;

import java.util.List;
import java.util.function.Consumer;

@OnlyIn(Dist.CLIENT)
public class CompositionHandler {

    public static void addToTooltip(List<Component> toolTip, ItemStack stack, HolderLookup.Provider registries) {
        boolean hasSpecificComposition = itemComposition(toolTip, stack);
        if (!hasSpecificComposition) {
            materialComposition(toolTip, stack);
        }
    }

    public static void appendFluidTooltips(FluidStack fluidStack, Consumer<Component> tooltips) {
        Fluid fluid = fluidStack.getFluid();
        var material = MaterialHelper.getMaterial(fluid);
        if (material != null) {
            var composition = material.getComposition();
            if (composition != null) {
                LangBuilder compositionName = RutileClient.lang();
                createTooltip(compositionName, composition);
                if (!compositionName.string().isEmpty()) {
                    MutableComponent component = RutileClient.lang().space().space().space()
                            .add(compositionName)
                            .component();
                    if (!RutileConfig.client().elementColorForTooltip.get()) {
                        component = component.withStyle(style -> style.withColor(RutileConfig.client().tooltipColor.get()));
                    }
                    tooltips.accept(component);
                }
            }
        }
        if (fluid instanceof MaterialFluid attributedFluid) {
            attributedFluid.getAttributes().forEach(a -> a.appendFluidTooltips(tooltips));
        }
    }

    public static boolean materialComposition(List<Component> toolTip, ItemStack stack) {
        var materialEntry = MaterialHelper.getMaterialEntry(stack.getItem());
        if (!materialEntry.isEmpty()) {
            Composition composition = MaterialCompositionManager.getInstance().getComposition(materialEntry.material);
            if (composition != null) {
                LangBuilder compositionName = RutileClient.lang();
                createTooltip(compositionName, composition);
                add(toolTip, compositionName);
                return true;
            }
        }
        if (stack.getItem() instanceof BucketItem bucket) {
            var fluid = bucket.content;
            if (!(fluid instanceof EmptyFluid)) {
                appendFluidTooltips(new FluidStack(fluid, FluidType.BUCKET_VOLUME), toolTip::add);
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
            int outerColour = ColourUtil.blendAll(subComposition.getElements().stream().map(ElementStack::getColor).toList());
            var outerStyle = Style.EMPTY.withColor(outerColour);
            if (!RutileConfig.client().elementColorForTooltip.get()) {
                outerStyle = Style.EMPTY.withColor(RutileConfig.client().tooltipColor.get());
            }
            if (encaseInBrackets) subComp.add(Component.literal("(").setStyle(outerStyle));
            for (int j = 0; j < subComposition.getElements().size(); j++) {
                if (subComposition.getElements().get(j) == null) continue;
                ElementStack elementStack = subComposition.getElements().get(j);
                MutableComponent elementComp = Component.literal(elementStack.getDisplay());
                if (RutileConfig.client().elementColorForTooltip.get()) {
                    elementComp = elementComp.setStyle(Style.EMPTY.withColor(elementStack.getColor()));
                }
                subComp.add(elementComp);
            }
            if (encaseInBrackets) subComp.add(Component.literal(")").setStyle(outerStyle));
            if (subComposition.getAmount() > 1) subComp.add(Component.literal(RutileClient.toSmallDownNumbers(String.valueOf(subComposition.getAmount()))).setStyle(outerStyle));
            compositionName.add(subComp);
        }
    }
}
