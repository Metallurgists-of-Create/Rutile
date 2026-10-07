package dev.metallurgists.rutile.compat.jei.category;

import dev.metallurgists.rutile.RutileClient;
import dev.metallurgists.rutile.api.composition.Composition;
import dev.metallurgists.rutile.api.composition.element.ElementStack;
import dev.metallurgists.rutile.compat.jei.RutileJeiConstants;
import dev.metallurgists.rutile.util.GuiTexture;
import mezz.jei.api.gui.builder.IIngredientAcceptor;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotRichTooltipCallback;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.createmod.catnip.utility.layout.LayoutHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static net.minecraft.world.item.Items.AIR;

public interface ElementCompositionWrapper<T> {
    Composition<T> getComposition();

    default IDrawable getBackground() {
        return asDrawable(RutileJeiConstants.JEI_SLOT);
    }

    void setRecipe(IRecipeLayoutBuilder builder, IFocusGroup focuses);

    record Items(List<Item> items, Composition<Item> composition) implements ElementCompositionWrapper<Item> {
        @Override
        public Composition<Item> getComposition() {
            return composition;
        }

        @Override
        public void setRecipe(IRecipeLayoutBuilder builder, IFocusGroup focuses) {
            layoutItemFluidOutput().forEach(layoutEntry -> {
                IRecipeSlotBuilder slotBuilder = builder.addSlot(RecipeIngredientRole.INPUT, (177 / 2) + layoutEntry.posX() + 1, 1).setBackground(getBackground(), -1, -1);
                if (layoutEntry.item != null) {
                    addIngredient(layoutEntry.item.getItem(), slotBuilder);
                }
                if (layoutEntry.fluid != null) {
                    slotBuilder.addFluidStack(layoutEntry.fluid.getFluid(), 1000);
                }
            });
            addElements(getComposition(), builder);
        }

        private List<ItemFluidLayoutEntry> layoutItemFluidOutput() {
            List<ItemFluidLayoutEntry> positions = new ArrayList<>(items.size());
            if (items.isEmpty()) {
                return positions;
            }
            LayoutHelper layout = LayoutHelper.centeredHorizontal(items.size(), 1, 18, 18, 1);
            for (Item item : items) {
                positions.add(new ItemFluidLayoutEntry(item.getDefaultInstance(), null, layout.getX(), layout.getY()));
                layout.next();
            }
            return positions;
        }
    }

    record Fluids(List<Fluid> fluids, Composition<Fluid> composition) implements ElementCompositionWrapper<Fluid> {
        @Override
        public Composition<Fluid> getComposition() {
            return composition;
        }

        @Override
        public void setRecipe(IRecipeLayoutBuilder builder, IFocusGroup focuses) {
            layoutItemFluidOutput().forEach(layoutEntry -> {
                IRecipeSlotBuilder slotBuilder = builder.addSlot(RecipeIngredientRole.INPUT, (177 / 2) + layoutEntry.posX() + 1, 1).setBackground(getBackground(), -1, -1);
                if (layoutEntry.item != null) {
                    addIngredient(layoutEntry.item.getItem(), slotBuilder);
                }
                if (layoutEntry.fluid != null) {
                    slotBuilder.addFluidStack(layoutEntry.fluid.getFluid(), 1000);
                }
            });
            addElements(getComposition(), builder);
        }

        private List<ItemFluidLayoutEntry> layoutItemFluidOutput() {
            List<ItemFluidLayoutEntry> entries = new ArrayList<>();
            for (Fluid fluid : fluids) {
                Item bucket = fluid.getBucket();
                entries.add(new ItemFluidLayoutEntry(null, new FluidStack(fluid, 1000), 0, 0));
                if (bucket != AIR) {
                    entries.add(new ItemFluidLayoutEntry(bucket.getDefaultInstance(), null, 0, 0));
                }
            }
            if (entries.isEmpty()) {
                return entries;
            }
            LayoutHelper layout = LayoutHelper.centeredHorizontal(entries.size(), 1, 18, 18, 1);
            for (int i = 0; i < entries.size(); i++) {
                ItemFluidLayoutEntry entry = entries.get(i);
                entries.set(i, new ItemFluidLayoutEntry(entry.item(), entry.fluid(), layout.getX(), layout.getY()));
                layout.next();
            }
            return entries;
        }
    }

    private static void addIngredient(Item item, IIngredientAcceptor<?> slotBuilder) {
        slotBuilder.addItemStack(item.getDefaultInstance());
    }

    static IRecipeSlotRichTooltipCallback addPercentageTooltipCallback(float percentage) {
        return (view, tooltip) -> {
            if (percentage != 1) {
                var percentageStr = percentage < 0.01 ? "<1" : (int) (percentage * 100);
                tooltip.add(RutileClient.getLang().text(percentageStr + "%").component()
                        .withStyle(ChatFormatting.GOLD));
            }

        };
    }

    private static List<LayoutEntry> layoutOutput(Map<ElementStack, Integer> elements, int totalElementsAmount) {
        int size = elements.size();
        List<LayoutEntry> positions = new ArrayList<>(size);
        LayoutHelper layout = LayoutHelper.centeredHorizontal(size, 1, 18, 18, 1);
        for (Map.Entry<ElementStack, Integer> element : elements.entrySet().stream().sorted((c, n) -> Integer.compare(n.getValue(), c.getValue())).toList()) {
            float percentage = (float) element.getValue() / totalElementsAmount;
            positions.add(new LayoutEntry(element.getKey().copyWithAmount(element.getValue()), percentage, layout.getX(), layout.getY()));
            layout.next();
        }

        return positions;
    }

    private static void addElements(Composition<?> composition, IRecipeLayoutBuilder builder) {
        Map<ElementStack, Integer> elementCounts = composition.getContainedElements();
        int totalElementsAmount = elementCounts.values().stream().reduce(Integer::sum).orElse(0);

        int xOffset = 177 / 2;
        int yOffset = 18 * 2;
        layoutOutput(elementCounts, totalElementsAmount).forEach(layoutEntry -> builder
                .addSlot(RecipeIngredientRole.OUTPUT, (xOffset) + layoutEntry.posX() + 1, yOffset + layoutEntry.posY() + 1)
                .setBackground(asDrawable(RutileJeiConstants.JEI_SLOT), -1, -1)
                .addIngredient(RutileJeiConstants.ELEMENT, layoutEntry.output)
                .addRichTooltipCallback(addPercentageTooltipCallback(layoutEntry.percentage))
        );
    }

    record LayoutEntry(
            ElementStack output,
            float percentage,
            int posX,
            int posY
    ) {}

    record ItemFluidLayoutEntry(
            @Nullable ItemStack item,
            @Nullable FluidStack fluid,
            int posX,
            int posY
    ) {}

    record MaterialLayoutEntry(
            List<ItemStack> items,
            int posX,
            int posY
    ) {}

    private static void addListedOutput(List<ItemStack> stacks, IIngredientAcceptor<?> slotBuilder) {
        slotBuilder.addItemStacks(stacks);
    }

    static IDrawable asDrawable(final GuiTexture texture) {
        return new IDrawable() {
            public int getWidth() {
                return texture.getWidth();
            }

            public int getHeight() {
                return texture.getHeight();
            }

            @Override @ParametersAreNonnullByDefault
            public void draw(GuiGraphics graphics, int xOffset, int yOffset) {
                texture.render(graphics, xOffset, yOffset);
            }
        };
    }
}
