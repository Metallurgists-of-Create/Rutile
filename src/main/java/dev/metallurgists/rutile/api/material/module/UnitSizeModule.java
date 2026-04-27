package dev.metallurgists.rutile.api.material.module;

import dev.metallurgists.rutile.api.material.module.registry.RegistryModule;
import dev.metallurgists.rutile.registry.RutileModules;
import lombok.Getter;
import net.minecraft.core.Holder;

import java.util.HashMap;
import java.util.Map;

@Getter
public class UnitSizeModule implements MaterialModule<UnitSizeModule> {
    public static final long UNIT = 3628800;

    private final Map<Holder<RegistryModule.Key>, Long> sizes;

    public UnitSizeModule() {
        this.sizes = new HashMap<>();
    }

    public final UnitSizeModule setSize(Holder<RegistryModule.Key> key, long size) {
        this.sizes.put(key, size);
        return this;
    }

    public final long getSize(Holder<RegistryModule.Key> key) {
        if (!this.sizes.containsKey(key)) return UNIT;
        return this.sizes.get(key);
    }

    @Override
    public ModuleHolder<UnitSizeModule> getType() {
        return RutileModules.UNIT_SIZE;
    }

    @Override
    public ModuleBuilder<UnitSizeModule> builder() {
        return new Builder();
    }

    public static class Builder implements ModuleBuilder<UnitSizeModule> {
        private final Map<Holder<RegistryModule.Key>, Long> sizes = new HashMap<>();

        public final Builder setSize(Holder<RegistryModule.Key> key, long size) {
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
