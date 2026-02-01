package dev.metallurgists.rutile.api.memorizer;

import net.minecraft.world.level.block.Block;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class Memoizer {

    public static <T> MemoizedSupplier<T> memoize(Supplier<T> delegate) {
        return new MemoizedSupplier<>(delegate);
    }

    public static <T extends Block> MemoizedBlockSupplier<T> memoizeBlockSupplier(Supplier<T> delegate) {
        return new MemoizedBlockSupplier<>(delegate);
    }

    public static <T, R> Function<T, R> memoizeFunctionWeakIdent(final Function<T, R> memoFunction) {
        return new Function<>() {

            private final Map<T, R> cache = new ConcurrentWeakIdentityHashMap<>();

            public R apply(T key) {
                return this.cache.computeIfAbsent(key, memoFunction);
            }

            public String toString() {
                return "memoizeFunctionWeakIdent/1[function=" + memoFunction + ", size=" + this.cache.size() + "]";
            }
        };
    }
}
