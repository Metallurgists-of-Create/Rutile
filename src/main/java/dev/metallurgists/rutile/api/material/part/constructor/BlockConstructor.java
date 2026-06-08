package dev.metallurgists.rutile.api.material.part.constructor;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

@FunctionalInterface
public interface BlockConstructor extends Constructor<Block, BlockBehaviour.Properties> {
}
