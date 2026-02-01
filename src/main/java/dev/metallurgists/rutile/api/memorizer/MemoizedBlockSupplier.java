package dev.metallurgists.rutile.api.memorizer;

import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class MemoizedBlockSupplier<T extends Block> extends MemoizedSupplier<T> {

    protected MemoizedBlockSupplier(Supplier<T> delegate) {
        super(delegate);
    }
}
