package dev.metallurgists.rutile.api.registrate;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.nullness.NonNullBiFunction;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import dev.metallurgists.rutile.api.composition.element.Element;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.base.MaterialFlags;
import dev.metallurgists.rutile.api.material.flag.FlagKey;
import dev.metallurgists.rutile.api.material.flag.IMaterialFlag;
import dev.metallurgists.rutile.api.registrate.builder.ElementBuilder;
import dev.metallurgists.rutile.api.registrate.builder.FlagKeyBuilder;
import dev.metallurgists.rutile.api.registrate.builder.MaterialBuilder;

public class RutileRegistrate extends AbstractRegistrate<RutileRegistrate> {
    /**
     * Construct a new Registrate for the given mod ID.
     *
     * @param modid The mod ID for which objects will be registered
     */
    protected RutileRegistrate(String modid) {
        super(modid);
    }

    public static RutileRegistrate create(String modid) {
        return new RutileRegistrate(modid);
    }

    // Elements
    public <T extends Element> ElementBuilder<T, RutileRegistrate> element(NonNullFunction<Element.Properties, T> factory) {
        return element(self(), factory);
    }

    public <T extends Element> ElementBuilder<T, RutileRegistrate> element(String name, NonNullFunction<Element.Properties, T> factory) {
        return element(self(), name, factory);
    }

    public <T extends Element, P> ElementBuilder<T, P> element(P parent, NonNullFunction<Element.Properties, T> factory) {
        return element(parent, currentName(), factory);
    }

    public <T extends Element, P> ElementBuilder<T, P> element(P parent, String name, NonNullFunction<Element.Properties, T> factory) {
        return entry(name, callback -> ElementBuilder.create(this, parent, name, callback, factory));
    }

    // Flag Keys
    public <C extends IMaterialFlag, T extends FlagKey<? extends IMaterialFlag>> FlagKeyBuilder<C, T, RutileRegistrate> flagKey(NonNullFunction<Class<C>, T> factory, Class<C> type) {
        return flagKey(self(), factory, type);
    }

    public <C extends IMaterialFlag, T extends FlagKey<? extends IMaterialFlag>> FlagKeyBuilder<C, T, RutileRegistrate> flagKey(String name, NonNullFunction<Class<C>, T> factory, Class<C> type) {
        return flagKey(self(), name, factory, type);
    }

    public <C extends IMaterialFlag, T extends FlagKey<? extends IMaterialFlag>, P> FlagKeyBuilder<C, T, P> flagKey(P parent, NonNullFunction<Class<C>, T> factory, Class<C> type) {
        return flagKey(parent, currentName(), factory, type);
    }

    public <C extends IMaterialFlag, T extends FlagKey<? extends IMaterialFlag>, P> FlagKeyBuilder<C, T, P> flagKey(P parent, String name, NonNullFunction<Class<C>, T> factory, Class<C> type) {
        return entry(name, callback -> FlagKeyBuilder.create(this, parent, name, callback, factory, type));
    }

    // Materials
    public <T extends Material> MaterialBuilder<T, RutileRegistrate> material(NonNullBiFunction<Material.MaterialInfo, MaterialFlags, T> factory) {
        return material(self(), factory);
    }

    public <T extends Material> MaterialBuilder<T, RutileRegistrate> material(String name, NonNullBiFunction<Material.MaterialInfo, MaterialFlags, T> factory) {
        return material(self(), name, factory);
    }

    public <T extends Material, P> MaterialBuilder<T, P> material(P parent, NonNullBiFunction<Material.MaterialInfo, MaterialFlags, T> factory) {
        return material(parent, currentName(), factory);
    }

    public <T extends Material, P> MaterialBuilder<T, P> material(P parent, String name, NonNullBiFunction<Material.MaterialInfo, MaterialFlags, T> factory) {
        return entry(name, callback -> MaterialBuilder.create(this, parent, name, callback, factory));
    }
}
