package dev.metallurgists.rutile.api.registrate;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import dev.metallurgists.rutile.api.composition.element.Element;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.registrate.element.ElementBuilder;
import dev.metallurgists.rutile.api.registrate.material.MaterialBuilder;
import net.minecraftforge.eventbus.api.IEventBus;

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

    @Override
    public RutileRegistrate registerEventListeners(IEventBus bus) {
        return super.registerEventListeners(bus);
    }

    public <T extends Material> MaterialBuilder<T, RutileRegistrate> material(NonNullFunction<Material.Builder, T> factory) {
        return material(self(), factory);
    }

    public <T extends Material> MaterialBuilder<T, RutileRegistrate> material(String name, NonNullFunction<Material.Builder, T> factory) {
        return material(self(), name, factory);
    }

    public <T extends Material, P> MaterialBuilder<T, P> material(P parent, NonNullFunction<Material.Builder, T> factory) {
        return material(parent, currentName(), factory);
    }

    public <T extends Material, P> MaterialBuilder<T, P> material(P parent, String name, NonNullFunction<Material.Builder, T> factory) {
        return entry(name, callback -> MaterialBuilder.create(this, parent, name, callback, factory));
    }

    public <T extends Element> ElementBuilder<T, RutileRegistrate> element(String symbol, NonNullFunction<Element.Properties, T> factory) {
        return element(self(), symbol, factory);
    }

    public <T extends Element> ElementBuilder<T, RutileRegistrate> element(String name, String symbol, NonNullFunction<Element.Properties, T> factory) {
        return element(self(), name, symbol, factory);
    }

    public <T extends Element, P> ElementBuilder<T, P> element(P parent, String symbol, NonNullFunction<Element.Properties, T> factory) {
        return element(parent, currentName(), symbol, factory);
    }

    public <T extends Element, P> ElementBuilder<T, P> element(P parent, String name, String symbol, NonNullFunction<Element.Properties, T> factory) {
        return entry(name, callback -> ElementBuilder.create(this, parent, name, symbol, callback, factory));
    }
}
