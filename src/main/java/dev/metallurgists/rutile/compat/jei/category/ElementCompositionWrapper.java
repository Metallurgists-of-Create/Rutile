package dev.metallurgists.rutile.compat.jei.category;

import dev.metallurgists.rutile.RutileClient;
import dev.metallurgists.rutile.api.composition.Composition;
import dev.metallurgists.rutile.api.composition.SubComposition;
import dev.metallurgists.rutile.api.element.Element;
import dev.metallurgists.rutile.api.element.ElementStack;
import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.material.MaterialHelper;
import dev.metallurgists.rutile.api.material.stack.MaterialEntry;
import dev.metallurgists.rutile.api.registry.RutileRegistries;
import dev.metallurgists.rutile.api.tag.TagPrefix;
import dev.metallurgists.rutile.compat.jei.RutileJeiConstants;
import dev.metallurgists.rutile.util.GuiTexture;
import mezz.jei.api.gui.builder.IIngredientAcceptor;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotRichTooltipCallback;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.createmod.catnip.layout.LayoutHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.minecraft.world.item.Items.AIR;

public interface ElementCompositionWrapper<T> {
    Composition getComposition();

    T getIngredient();

    default IDrawable getBackground() {
        return asDrawable(RutileJeiConstants.JEI_SLOT);
    }

    void setRecipe(IRecipeLayoutBuilder builder, IFocusGroup focuses);

    record Items(Item item, Composition composition) implements ElementCompositionWrapper<Item> {
        @Override
        public Composition getComposition() {
            return composition;
        }

        @Override
        public Item getIngredient() {
            return item;
        }

        @Override
        public void setRecipe(IRecipeLayoutBuilder builder, IFocusGroup focuses) {
            layoutItemFluidOutput(new ItemStack(getIngredient())).forEach(layoutEntry -> {
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

        private List<ItemFluidLayoutEntry> layoutItemFluidOutput(ItemStack item) {
            int size = 1;
            var itemTank = item.getCapability(Capabilities.FluidHandler.ITEM);
            FluidStack fluid = itemTank != null ? itemTank.getFluidInTank(0) : null;
            if (fluid != null) size = 2;
            List<ItemFluidLayoutEntry> positions = new ArrayList<>(size);
            LayoutHelper layout = LayoutHelper.centeredHorizontal(size, 1, 18, 18, 1);
            if (!item.isEmpty()) {
                positions.add(new ItemFluidLayoutEntry(item, null, layout.getX(), layout.getY()));
                layout.next();
            }
            if (fluid != null && !fluid.isEmpty()) {
                positions.add(new ItemFluidLayoutEntry(null, fluid, layout.getX(), layout.getY()));
                layout.next();
            }
            return positions;
        }
    }

    record Materials(Material material, Composition composition) implements ElementCompositionWrapper<Material> {
        @Override
        public Composition getComposition() {
            return composition;
        }

        @Override
        public Material getIngredient() {
            return material;
        }

        public List<ItemStack> getAllMaterialItems() {
            List<ItemStack> items = new ArrayList<>();
            for (TagPrefix tagPrefix : RutileRegistries.TAG_PREFIXES) {
                MaterialEntry materialEntry = new MaterialEntry(tagPrefix, getIngredient());
                List<Item> entryItems = new ArrayList<>(MaterialHelper.getItems(materialEntry).stream().map(ItemLike::asItem).toList());
                items.addAll(entryItems.stream().map(Item::getDefaultInstance).toList());
            }
            return items;
        }

        @Override
        public void setRecipe(IRecipeLayoutBuilder builder, IFocusGroup focuses) {
            layoutMaterialOutput(getAllMaterialItems()).forEach(layoutEntry -> {
                IRecipeSlotBuilder slotBuilder = builder.addSlot(RecipeIngredientRole.INPUT, (177 / 2) + layoutEntry.posX() + 1, 1).setBackground(getBackground(), -1, -1);
                if (!layoutEntry.items.isEmpty()) {
                    addListedOutput(layoutEntry.items, slotBuilder);
                }
            });
            addElements(getComposition(), builder);
        }

        private List<MaterialLayoutEntry> layoutMaterialOutput(List<ItemStack> items) {
            int size = 1;
            List<MaterialLayoutEntry> positions = new ArrayList<>(size);
            LayoutHelper layout = LayoutHelper.centeredHorizontal(size, 1, 18, 18, 0);
            positions.add(new MaterialLayoutEntry(items, layout.getX(), layout.getY()));
            layout.next();
            return positions;
        }
    }

    record Fluids(Fluid fluid, Composition composition) implements ElementCompositionWrapper<Fluid> {
        @Override
        public Composition getComposition() {
            return composition;
        }

        @Override
        public Fluid getIngredient() {
            return fluid;
        }

        @Override
        public void setRecipe(IRecipeLayoutBuilder builder, IFocusGroup focuses) {
            layoutItemFluidOutput(getIngredient()).forEach(layoutEntry -> {
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

        private List<ItemFluidLayoutEntry> layoutItemFluidOutput(Fluid fluid) {
            int size = 1;
            Item bucket = fluid.getBucket();
            if (bucket != AIR) size = 2;
            List<ItemFluidLayoutEntry> positions = new ArrayList<>(size);
            LayoutHelper layout = LayoutHelper.centeredHorizontal(size, 1, 18, 18, 1);
            positions.add(new ItemFluidLayoutEntry(null, new FluidStack(fluid, 1000), layout.getX(), layout.getY()));
            layout.next();
            if (bucket != AIR) {
                positions.add(new ItemFluidLayoutEntry(bucket.getDefaultInstance(), null, layout.getX(), layout.getY()));
                layout.next();
            }
            return positions;
        }
    }

    private static void addIngredient(Item item, IIngredientAcceptor<?> slotBuilder) {
        slotBuilder.addItemStack(item.getDefaultInstance());
    }

    static IRecipeSlotRichTooltipCallback addPercentageTooltipCallback(float percentage) {
        return (view, tooltip) -> {
            if (percentage != 1) {
                var percentageStr = percentage < 0.01 ? "<1" : (int) (percentage * 100);
                tooltip.add(RutileClient.lang().text(percentageStr + "%").component()
                        .withStyle(ChatFormatting.GOLD));
            }

        };
    }

    private static List<LayoutEntry> layoutOutput(Map<Element, Integer> elements, int totalElementsAmount) {
        int size = elements.size();
        List<LayoutEntry> positions = new ArrayList<>(size);
        LayoutHelper layout = LayoutHelper.centeredHorizontal(size, 1, 18, 18, 1);
        for (Map.Entry<Element, Integer> element : elements.entrySet()) {
            float percentage = (float) element.getValue() / totalElementsAmount;
            positions.add(new LayoutEntry(element.getKey().asStack(), percentage, layout.getX(), layout.getY()));
            layout.next();
        }

        return positions;
    }

    private static void addElements(Composition composition, IRecipeLayoutBuilder builder) {
        Map<Element, Integer> elementCounts = new HashMap<>();
        int totalElementsAmount = 0;
        for (SubComposition subComposition : composition.compositions()) {
            for (ElementStack elementStack : subComposition.getElements()) {
                Element element = elementStack.getElement();
                int amount = elementStack.getAmount();
                elementCounts.put(element, elementCounts.getOrDefault(element, 0) + amount);
                totalElementsAmount += amount;
            }
        }
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

            @Override
            public void draw(GuiGraphics graphics, int xOffset, int yOffset) {
                texture.render(graphics, xOffset, yOffset);
            }
        };
    }
}
