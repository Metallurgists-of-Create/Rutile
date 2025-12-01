package dev.metallurgists.rutile.api.memorizer;

import net.minecraft.world.level.block.Block;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class Memorizer {

    public static <T> MemorizedSupplier<T> memorize(Supplier<T> delegate) {
        return new MemorizedSupplier<>(delegate);
    }

    public static <T extends Block> MemorizedBlockSupplier<T> memorizeBlockSupplier(Supplier<T> delegate) {
        return new MemorizedBlockSupplier<>(delegate);
    }

    public static <T, R> Function<T, R> memorizeFunctionWeakIdent(final Function<T, R> memoFunction) {
        return new Function<>() {

            private final Map<T, R> cache = new ConcurrentWeakIdentityHashMap<>();

            public R apply(T key) {
                return this.cache.computeIfAbsent(key, memoFunction);
            }

            public String toString() {
                return "memorizeFunctionWeakIdent/1[function=" + memoFunction + ", size=" + this.cache.size() + "]";
            }
        };
    }
}
