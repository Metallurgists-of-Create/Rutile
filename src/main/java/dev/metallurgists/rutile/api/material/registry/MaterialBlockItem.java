package dev.metallurgists.rutile.api.material.registry;

import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.tag.TagPrefix;
import net.minecraft.network.chat.Component;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class MaterialBlockItem extends BlockItem {
    public final TagPrefix tagPrefix;
    public final Material material;

    public MaterialBlockItem(Block block, Properties properties, TagPrefix tagPrefix, Material material) {
        super(block, properties);
        this.tagPrefix = tagPrefix;
        this.material = material;
    }

    // This should only be enabled if the material is
    @Override
    public boolean isEnabled(FeatureFlagSet enabledFeatures) {
        return material.isEnabled(enabledFeatures) && super.isEnabled(enabledFeatures);
    }

    @Override
    @NotNull
    public MaterialBlock getBlock() {
        return (MaterialBlock) super.getBlock();
    }

    @Override
    public String getDescriptionId() {
        return getBlock().getDescriptionId();
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        return getDescriptionId();
    }

    @Override
    public Component getDescription() {
        return getBlock().getName();
    }

    @Override
    public Component getName(ItemStack stack) {
        return getDescription();
    }
}
