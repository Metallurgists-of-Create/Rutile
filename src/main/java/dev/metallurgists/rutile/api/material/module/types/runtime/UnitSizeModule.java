package dev.metallurgists.rutile.api.material.module.types.runtime;

import dev.metallurgists.rutile.api.material.module.MaterialModule;
import dev.metallurgists.rutile.api.material.part.PartKey;
import dev.metallurgists.rutile.registry.RutileModules;

import java.util.HashMap;
import java.util.Map;

public class UnitSizeModule implements MaterialModule<UnitSizeModule> {
    public static final long UNIT = 3628800;

    private final Map<PartKey<?>, Long> sizes;

    public UnitSizeModule() {
        this.sizes = new HashMap<>();
    }

    public final UnitSizeModule setSize(PartKey<?> key, long size) {
        this.sizes.put(key, size);
        return this;
    }

    public final long getSize(PartKey<?> key) {
        if (!this.sizes.containsKey(key)) return UNIT;
        return this.sizes.get(key);
    }

    @Override
    public UnitSizeModule getType() {
        return RutileModules.UNIT_SIZE;
    }

    @Override
    public ModuleBuilder<UnitSizeModule> builder() {
        return new Builder();
    }

    public static class Builder implements ModuleBuilder<UnitSizeModule> {
        private final Map<PartKey<?>, Long> sizes = new HashMap<>();

        public final Builder setSize(PartKey<?> key, long size) {
            this.sizes.put(key, size);
            return this;
        }

        @Override
        public UnitSizeModule build() {
            UnitSizeModule module = new UnitSizeModule();
            module.sizes.putAll(sizes);
            return module;
        }
    }
}
