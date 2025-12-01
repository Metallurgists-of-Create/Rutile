package dev.metallurgists.rutile.api.memorizer;

import java.util.function.Supplier;

public class MemorizedSupplier<T> implements Supplier<T> {

    protected T value = null;
    protected boolean initialized = false;
    protected final Supplier<T> delegate;

    protected MemorizedSupplier(Supplier<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public T get() {
        if (!initialized) {
            value = delegate.get();
            initialized = true;
        }
        return value;
    }

    public void invalidate() {
        initialized = false;
        value = null;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + (initialized ? value : "Uninitialized") + ")";
    }
}
