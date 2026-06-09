package dev.metallurgists.rutile.api.part.type;

import dev.metallurgists.rutile.api.material.objects.MaterialBlock;
import dev.metallurgists.rutile.api.part.Part;
import dev.metallurgists.rutile.api.part.constructor.BlockConstructor;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;

public abstract class BlockPart extends Part<Block> {

    @Override
    public BlockConstructor constructor() {
        return MaterialBlock::new;
    }

    @Override
    public ResourceKey<Registry<Block>> registryResourceKey() {
        return Registries.BLOCK;
    }
}
