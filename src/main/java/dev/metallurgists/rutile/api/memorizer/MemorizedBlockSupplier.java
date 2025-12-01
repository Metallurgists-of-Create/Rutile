package dev.metallurgists.rutile.api.memorizer;

import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class MemorizedBlockSupplier<T extends Block> extends MemorizedSupplier<T> {

    protected MemorizedBlockSupplier(Supplier<T> delegate) {
        super(delegate);
    }
}
