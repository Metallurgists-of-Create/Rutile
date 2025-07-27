package dev.metallurgists.rutile.api.material.registry.fluid;

import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.flag.types.IFluidRegistry;
import net.createmod.catnip.theme.Color;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class MaterialBucketItem extends BucketItem {
    public final Material material;
    public final IFluidRegistry fluidFlag;

    public MaterialBucketItem(Supplier<? extends Fluid> supplier, Properties builder, Material material, IFluidRegistry flag) {
        super(supplier, builder);
        this.material = material;
        this.fluidFlag = flag;
    }

    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return this.getClass() == MaterialBucketItem.class ? new FluidBucketWrapper(stack) : super.initCapabilities(stack, nbt);
    }

    public String getUnlocalizedName() {
        var keyLoc = fluidFlag.getKey().getId();
        return "materialflag." + keyLoc.getNamespace() + ".bucket." + keyLoc.getPath();
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
}
