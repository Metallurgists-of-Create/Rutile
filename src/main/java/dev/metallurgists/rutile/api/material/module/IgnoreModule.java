package dev.metallurgists.rutile.api.material.module;

import dev.metallurgists.rutile.api.material.module.registry.RegistryModule;
import dev.metallurgists.rutile.registry.RutileModules;
import net.minecraft.core.Holder;

import java.util.*;

public class IgnoreModule implements MaterialModule<IgnoreModule> {

    private final List<Holder<RegistryModule.Key>> ignored;

    public IgnoreModule() {
        this.ignored = new ArrayList<>();
    }

    @SafeVarargs
    public final IgnoreModule ignore(Holder<RegistryModule.Key>... keys) {
        ignored.addAll(Arrays.asList(keys));
        return this;
    }

    @SafeVarargs
    public final IgnoreModule acknowledge(Holder<RegistryModule.Key>... keys) {
        ignored.removeAll(Arrays.asList(keys));
        return this;
    }

    public boolean isIgnored(Holder<RegistryModule.Key> keyHolder) {
        return ignored.contains(keyHolder);
    }

    @Override
    public ModuleHolder<IgnoreModule> getType() {
        return RutileModules.IGNORE;
    }

    @Override
    public ModuleBuilder<IgnoreModule> builder() {
        return new Builder();
    }

    public static class Builder implements ModuleBuilder<IgnoreModule> {
        private final List<Holder<RegistryModule.Key>> ignored = new ArrayList<>();

        @SafeVarargs
        public final Builder ignore(Holder<RegistryModule.Key>... keys) {
            ignored.addAll(Arrays.asList(keys));
            return this;
        }

        @Override
        public IgnoreModule build() {
            IgnoreModule module = new IgnoreModule();
            module.ignored.addAll(ignored);
            return module;
        }
    }
}
