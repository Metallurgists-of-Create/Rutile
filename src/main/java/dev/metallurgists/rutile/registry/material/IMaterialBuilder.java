package dev.metallurgists.rutile.registry.material;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.material.MaterialEntry;
import dev.metallurgists.rutile.api.material.module.MaterialModule;
import dev.metallurgists.rutile.api.material.variable.VariableKey;
import dev.metallurgists.rutile.registry.deferred.ModuleHolder;

import java.util.function.Function;

public interface IMaterialBuilder<S extends IMaterialBuilder<S>> {
    <T extends MaterialModule<T>> S module(ModuleHolder<T> module, Function<T, T> action);
    <T> S variable(VariableKey<T> key, T value);
    MaterialEntry register();
    RegistryEntry<Material, Material> reg();
}
