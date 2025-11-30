package dev.metallurgists.rutile.api.material.builder;

import dev.metallurgists.rutile.api.dynamic_pack.asset.RutileDynamicResourcePack;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.util.helpers.ModelHelpers;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public abstract class MaterialBlockBuilder extends MaterialRegistryBuilder<Block> {

    protected MaterialBlockBuilder(String nameFormat) {
        super(Registries.BLOCK, nameFormat);
    }

    @Override
    public void registerAssets(Material material) {
        ModelHelpers.cubeAllBlockModel(material, getBuilderName(), nameFormat());
    }
}
