package dev.metallurgists.rutile.api.material.objects;

import dev.metallurgists.rutile.api.material.MaterialData;
import dev.metallurgists.rutile.api.material.module.types.runtime.UnitSizeModule;
import dev.metallurgists.rutile.api.material.part.PartKey;
import dev.metallurgists.rutile.registry.RutileModules;
import dev.metallurgists.rutile.registry.RutileVariableKeys;
import net.minecraft.network.chat.Component;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;

import javax.annotation.Nullable;

public class MaterialItem extends Item {

    public final PartKey<Item> partKey;
    public final MaterialData data;

    public MaterialItem(Properties properties, PartKey<Item> partKey, MaterialData data) {
        super(properties);
        this.partKey = partKey;
        this.data = data;
    }

    @Override
    public boolean isEnabled(FeatureFlagSet enabledFeatures) {
        return data.isEnabled(enabledFeatures) && super.isEnabled(enabledFeatures);
    }

    @Override
    public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
        return getItemBurnTime();
    }

    @Override
    public String getDescriptionId() {
        return partKey.getPart(data.getName()).getUnlocalizedName(data);
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        return partKey.getPart(data.getName()).getUnlocalizedName(data);
    }

    @Override
    public Component getDescription() {
        return partKey.getPart(data.getName()).getLocalizedName(data);
    }

    @Override
    public Component getName(ItemStack stack) {
        return getDescription();
    }

    public int getItemBurnTime() {
        int burnTime = data.getVariable(RutileVariableKeys.BURN_TIME);
        long size = data.getModule(RutileModules.UNIT_SIZE).map(module -> module.getSize(this.partKey)).orElse(UnitSizeModule.UNIT);
        if (burnTime != 0)
            return (int) (burnTime * size / UnitSizeModule.UNIT);
        return 0;
    }
}
