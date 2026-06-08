package dev.metallurgists.rutile.api.material.part.type;

import dev.metallurgists.rutile.api.material.objects.MaterialBlock;
import dev.metallurgists.rutile.api.material.objects.MaterialItem;
import dev.metallurgists.rutile.api.material.part.Part;
import dev.metallurgists.rutile.api.material.part.constructor.BlockConstructor;
import dev.metallurgists.rutile.api.material.part.constructor.ItemConstructor;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
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
