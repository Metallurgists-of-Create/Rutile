package dev.metallurgists.rutile.api.fluid;

import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.material.flags.FlagKey;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import org.jetbrains.annotations.Nullable;

public class MaterialBucketItem extends BucketItem {

    final Material material;
    final String langKey;

    public MaterialBucketItem(Fluid fluid, Item.Properties properties, Material material, String langKey) {
        super(fluid, properties);
        this.material = material;
        this.langKey = langKey;
    }

    public static int color(ItemStack itemStack, int index) {
        if (itemStack.getItem() instanceof MaterialBucketItem item) {
            if (index == 1) {
                return IClientFluidTypeExtensions.of(item.content).getTintColor();
            }
        }
        return -1;
    }

    @Override
    public String getDescriptionId() {
        return "item.rutile.bucket";
    }

    @Override
    public Component getDescription() {
        Component materialName = material.getDisplayName();
        return Component.translatable("item.rutile.bucket", Component.translatable(this.langKey, materialName));
    }

    @Override
    public Component getName(ItemStack stack) {
        return this.getDescription();
    }

    @Override
    public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
        var flag = material.getFlag(FlagKey.FLUID);
        if (flag != null) {
            var fluid = material.getFluid();
            if (fluid instanceof MaterialFluid materialFluid) {
                return materialFluid.getBurnTime();
            }
        }
        return 0;
    }
}
