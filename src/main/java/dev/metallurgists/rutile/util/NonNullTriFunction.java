package dev.metallurgists.rutile.util;

import com.tterrag.registrate.util.nullness.NonnullType;
import org.apache.commons.lang3.function.TriFunction;

@FunctionalInterface
public interface NonNullTriFunction<@NonnullType T, @NonnullType U, @NonnullType V, @NonnullType R> extends TriFunction<T, U, V, R> {

    @Override
    R apply(T t, U u, V v);
}
