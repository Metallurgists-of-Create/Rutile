package dev.metallurgists.rutile.api.material.registry;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.material.module.UnitSizeModule;
import dev.metallurgists.rutile.api.material.module.registry.RegistryModule;
import dev.metallurgists.rutile.registry.RutileModules;
import dev.metallurgists.rutile.registry.RutileVariableKeys;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MaterialItem extends Item {

    public final Holder<RegistryModule.Key> registerKey;
    public final Material material;

    public MaterialItem(Properties properties, Holder<RegistryModule.Key> registerKey, Material material) {
        super(properties);
        this.registerKey = registerKey;
        this.material = material;
        if (Rutile.isClientSide()) {
            //TagPrefixItemRenderer.create(this, tagPrefix.materialIconType(), material.getMaterialIconSet());
        }
    }

    // This should only be enabled if the material is
    @Override
    public boolean isEnabled(FeatureFlagSet enabledFeatures) {
        return material.isEnabled(enabledFeatures) && super.isEnabled(enabledFeatures);
    }

    @Override
    public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
        return getItemBurnTime();
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, context, tooltipComponents, isAdvanced);
        var tooltipModule = material.getModule(RutileModules.TOOLTIP);
        tooltipModule.ifPresent(tooltip -> {
            if (tooltip.tooltip() != null) {
                tooltip.tooltip().accept(material, tooltipComponents);
            }
        });
    }

    @Override
    public String getDescriptionId() {
        return registerKey.value().getUnlocalizedName(material);
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        return registerKey.value().getUnlocalizedName(material);
    }

    @Override
    public Component getDescription() {
        return registerKey.value().getLocalizedName(material);
    }

    @Override
    public Component getName(ItemStack stack) {
        return getDescription();
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);

    }

    public int getItemBurnTime() {
        int burnTime = material.getVariable(RutileVariableKeys.BURN_TIME);
        long size = material.getModule(RutileModules.UNIT_SIZE).map(module -> module.getSize(this.registerKey)).orElse(UnitSizeModule.UNIT);
        if (burnTime != 0)
            return (int) (burnTime * size / UnitSizeModule.UNIT);
        return 0;
    }
}
