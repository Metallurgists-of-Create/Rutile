package dev.metallurgists.rutile.registry.material;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.material.MaterialEntry;
import dev.metallurgists.rutile.api.material.MaterialProperties;
import dev.metallurgists.rutile.api.material.module.MaterialModule;
import dev.metallurgists.rutile.api.material.variable.VariableKey;
import dev.metallurgists.rutile.registry.deferred.ModuleHolder;

import java.util.function.Function;

public class DummyMaterialBuilder implements IMaterialBuilder<DummyMaterialBuilder> {

    MaterialEntry material;
    MaterialProperties properties;

    public DummyMaterialBuilder(MaterialEntry m) {
        this.material = m;
        properties = m.get().getProperties();
    }

    /**
     * @param module
     * @param action
     * @param <T>
     */
    @Override
    public <T extends MaterialModule<T>> DummyMaterialBuilder module(ModuleHolder<T> module, Function<T, T> action) {
        properties.addModule(module, action);
        return this;
    }

    /**
     * @param key
     * @param value
     * @param <T>
     */
    @Override
    public <T> DummyMaterialBuilder variable(VariableKey<T> key, T value) {
        properties.addVariable(key, value);
        return this;
    }

    /**
     * @return
     */
    @Override
    public MaterialEntry register() {
        return material;
    }

    public RegistryEntry<Material, Material> reg() {
        //nuh uh
        return material;
    }
}
