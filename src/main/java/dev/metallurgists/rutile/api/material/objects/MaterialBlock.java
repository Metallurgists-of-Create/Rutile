package dev.metallurgists.rutile.api.material.objects;

import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.part.PartKey;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.block.Block;

public class MaterialBlock extends Block {

    public final PartKey<Block> partKey;
    public final Material data;

    public MaterialBlock(Properties properties, PartKey<Block> partKey, Material data) {
        super(properties);
        this.partKey = partKey;
        this.data = data;
    }

    @Override
    public boolean isEnabled(FeatureFlagSet enabledFeatures) {
        return data.isEnabled(enabledFeatures) && super.isEnabled(enabledFeatures);
    }

    @Override
    public String getDescriptionId() {
        return partKey.getPart(data.getName()).getUnlocalizedName(data);
    }

    @Override
    public MutableComponent getName() {
        return partKey.getPart(data.getName()).getLocalizedName(data);
    }
}
