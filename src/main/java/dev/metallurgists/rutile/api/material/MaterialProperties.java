package dev.metallurgists.rutile.api.material;

import dev.metallurgists.rutile.api.material.module.MaterialModule;
import dev.metallurgists.rutile.api.material.variable.VariableKey;
import dev.metallurgists.rutile.registry.deferred.ModuleHolder;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public class MaterialProperties {
    private final Map<ModuleHolder<?>, MaterialModule<?>> modules;
    private final Map<VariableKey<?>, Object> variables;

    public MaterialProperties() {
        modules = new Object2ObjectOpenHashMap<>();
        variables = new Object2ObjectOpenHashMap<>();
    }

    public <T extends MaterialModule<T>> Optional<T> getModule(ModuleHolder<T> module) {
        if (this.modules.containsKey(module)) {
            return Optional.ofNullable((T) this.modules.get(module));
        }
        return Optional.empty();
    }

    public <T extends MaterialModule<T>> void addModule(ModuleHolder<T> module, Function<T, T> action) {
        if (this.modules.containsKey(module)) {
            T oldModule = (T)this.modules.get(module);
            this.modules.put(module, action.apply(oldModule));
        } else this.modules.put(module, action.apply(module.get()));
    }

    public <T extends MaterialModule<T>> void modifyModule(ModuleHolder<T> module, Function<T, T> action) {
        if (this.modules.containsKey(module)) {
            T oldModule = (T)this.modules.get(module);
            this.modules.put(module, action.apply(oldModule));
        }
    }

    public <T> void addVariable(VariableKey<T> key, T value) {
        this.variables.put(key, value);
    }

    public <T> void addVariable(VariableKey<T> key, T value, Function<T, T> ifPresent) {
        if (this.variables.containsKey(key) && this.variables.get(key).getClass().isAssignableFrom(key.clazz())) {
            T oldValue = (T)this.variables.get(key);
            this.variables.put(key, ifPresent.apply(oldValue));
        } else this.variables.put(key, value);
    }

    public <T> T getVariable(VariableKey<T> key) {
        if (this.variables.containsKey(key) && this.variables.get(key).getClass().isAssignableFrom(key.clazz())) {
            return (T)this.variables.get(key);
        }
        return key.defaultValue();
    }
}
