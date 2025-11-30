package dev.metallurgists.rutile.api.material.registry.fluid;

import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.builder.MaterialFluidBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

import javax.annotation.Nullable;

public class MaterialBucketItem extends BucketItem {
    public final Material material;
    public final MaterialFluidBuilder fluidBuilder;

    public MaterialBucketItem(BaseFlowingFluid fluid, Properties builder, Material material, MaterialFluidBuilder fluidBuilder) {
        super(fluid, builder);
        this.material = material;
        this.fluidBuilder = fluidBuilder;
    }

    public static int color(ItemStack itemStack, int index) {
        if (itemStack.getItem() instanceof MaterialBucketItem item) {
            if (index == 1) {
                return IClientFluidTypeExtensions.of(item.content).getTintColor();
            }
        }
        return -1;
    }

    public String getUnlocalizedName() {
        return "flagSource.fluid.%s.bucket".formatted(fluidBuilder.getFlagSource().rlForm());
    }

    public MutableComponent getLocalizedName(Material material) {
        return Component.translatable(getUnlocalizedName(), material.getDisplayName());
    }

    @Override
    public String getDescriptionId() {
        return getUnlocalizedName();
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        return getDescriptionId();
    }

    @Override
    public MutableComponent getDescription() {
        return getLocalizedName(material);
    }

    @Override
    public MutableComponent getName(ItemStack stack) {
        return getDescription();
    }

    @Override
    public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
        if (fluidBuilder != null) {
            var props = material.getFlags().getPropertiesFor(fluidBuilder.getFlagSource());
            return props.getBurnTime();
        }
        return 0;
    }
}
