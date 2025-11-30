package dev.metallurgists.rutile.api.material.registry.item;

import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.builder.MaterialItemBuilder;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class MaterialItem extends Item implements IMaterialItem {
    public final Material material;
    public final MaterialItemBuilder itemBuilder;

    public MaterialItem(Properties properties, Material material, MaterialItemBuilder itemBuilder) {
        super(properties);
        this.material = material;
        this.itemBuilder = itemBuilder;
    }

    @Override
    public String getDescriptionId() {
        return itemBuilder.getFlagSource().getUnlocalizedName(material, itemBuilder.nameFormat());
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        return getDescriptionId();
    }

    @Override
    public MutableComponent getDescription() {
        return itemBuilder.getFlagSource().getLocalizedName(material, itemBuilder.nameFormat());
    }

    @Override
    public MutableComponent getName(ItemStack stack) {
        return getDescription();
    }

    @Override
    public Material getMaterial() {
        return this.material;
    }

    @Override
    public MaterialItemBuilder getBuilder() {
        return this.itemBuilder;
    }
}
