package dev.metallurgists.rutile.registry.material;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.metallurgists.rutile.RutileRegistrate;
import dev.metallurgists.rutile.api.composition.Composition;
import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.material.MaterialEntry;
import dev.metallurgists.rutile.api.material.MaterialProperties;
import dev.metallurgists.rutile.api.material.module.MaterialModule;
import dev.metallurgists.rutile.api.material.variable.VariableKey;
import dev.metallurgists.rutile.registry.RutileRegistries;
import dev.metallurgists.rutile.registry.deferred.ModuleHolder;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

public class MaterialBuilder<R> extends AbstractBuilder<Material, Material, R, MaterialBuilder<R>> implements IMaterialBuilder<MaterialBuilder<R>> {
    private final Supplier<Material> material;
    private final MaterialProperties properties;

    public MaterialBuilder(AbstractRegistrate<?> owner, R parent, String name, BuilderCallback callback, Composition composition) {
        super(owner, parent, name, callback, RutileRegistries.MATERIALS);
        properties = new MaterialProperties();
        material = () -> new Material(name, composition, properties);
    }

    @Override
    public <T extends MaterialModule<T>> MaterialBuilder<R> module(ModuleHolder<T> module, Function<T, T> action) {
        properties.addModule(module, action);
        return this;
    }

    @Override
    public <T> MaterialBuilder<R> variable(VariableKey<T> key, T value) {
        properties.addVariable(key, value);
        return this;
    }
    /**
     * Create the built entry. This method will be lazily resolved at registration time, so it is safe to bake in values from the builder.
     *
     * @return The built entry
     */
    @Override
    protected @NotNull Material createEntry() {
        return material.get();
    }

    @Override
    public @NotNull MaterialEntry register() {
        return MaterialEntry.cast(super.register());
    }

    public RegistryEntry<Material, Material> reg() {
        return super.register();
    }
}
