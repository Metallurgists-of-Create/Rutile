package dev.metallurgists.rutile.api.material.registry.block;

import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.base.MaterialLike;
import dev.metallurgists.rutile.api.material.builder.MaterialBlockBuilder;
import dev.metallurgists.rutile.client.MaterialBlockRenderer;
import dev.metallurgists.rutile.util.ClientUtil;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class MaterialBlock extends Block implements IMaterialBlock {
    public final Material material;
    public final MaterialBlockBuilder blockBuilder;

    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;

    public MaterialBlock(Properties properties, MaterialLike material, MaterialBlockBuilder blockBuilder, boolean registerModel) {
        super(properties);
        this.material = material.asMaterial();
        this.blockBuilder = blockBuilder;
        if (registerModel && ClientUtil.isClientSide()) {
            MaterialBlockRenderer.create(this, material, blockBuilder.getFlagSource());
        }
    }

    @Override
    public String getDescriptionId() {
        return blockBuilder.getFlagSource().getUnlocalizedName(material, blockBuilder.nameFormat());
    }

    @Override
    public MutableComponent getName() {
        return blockBuilder.getFlagSource().getLocalizedName(material, blockBuilder.nameFormat());
    }

    public MaterialBlock(Properties properties, Material material, MaterialBlockBuilder blockBuilder) {
        this(properties, material, blockBuilder, true);
    }

    @Override
    public Material getMaterial() {
        return this.material;
    }

    @Override
    public MaterialBlockBuilder getBuilder() {
        return this.blockBuilder;
    }
}
