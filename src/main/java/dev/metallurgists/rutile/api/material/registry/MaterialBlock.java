package dev.metallurgists.rutile.api.material.registry;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.tag.TagPrefix;
import dev.metallurgists.rutile.client.MaterialBlockRenderer;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.block.Block;

public class MaterialBlock extends Block {
    public final TagPrefix tagPrefix;
    public final Material material;

    public MaterialBlock(Properties properties, TagPrefix tagPrefix, Material material, boolean registerModel) {
        super(properties);
        this.material = material;
        this.tagPrefix = tagPrefix;
        if (registerModel && Rutile.isClientSide()) {
            MaterialBlockRenderer.create(this, material, tagPrefix);
        }
    }

    public MaterialBlock(Properties properties, TagPrefix tagPrefix, Material material) {
        this(properties, tagPrefix, material, true);
    }

    // This should only be enabled if the material is
    @Override
    public boolean isEnabled(FeatureFlagSet enabledFeatures) {
        return material.isEnabled(enabledFeatures) && super.isEnabled(enabledFeatures);
    }

    @Override
    public String getDescriptionId() {
        return tagPrefix.getUnlocalizedName(material);
    }

    @Override
    public MutableComponent getName() {
        return tagPrefix.getLocalizedName(material);
    }
}
