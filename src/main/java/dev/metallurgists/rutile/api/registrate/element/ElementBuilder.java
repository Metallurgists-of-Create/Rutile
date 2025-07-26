package dev.metallurgists.rutile.api.registrate.element;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import com.tterrag.registrate.util.nullness.NonnullType;
import dev.metallurgists.rutile.api.composition.element.Element;
import dev.metallurgists.rutile.api.registry.RutileAPI;
import dev.metallurgists.rutile.api.registry.RutileRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegistryObject;

public class ElementBuilder<T extends Element, P> extends AbstractBuilder<Element, T, P, ElementBuilder<T, P>> {

    public static <T extends Element, P> ElementBuilder<T, P> create(AbstractRegistrate<?> owner, P parent, String name, String symbol, BuilderCallback callback, NonNullFunction<Element.Properties, T> factory) {
        return new ElementBuilder<>(owner, parent, name, symbol, callback, factory);
    }

    private final NonNullFunction<Element.Properties, T> factory;
    private final String symbol;

    private NonNullSupplier<Element.Properties> initialProperties = () -> new Element.Properties().id(getOwner(), getName());
    private NonNullFunction<Element.Properties, Element.Properties> propertiesCallback = NonNullUnaryOperator.identity();

    public ElementBuilder(AbstractRegistrate<?> owner, P parent, String name, String symbol, BuilderCallback callback, NonNullFunction<Element.Properties, T> factory) {
        super(owner, parent, name, callback, RutileRegistries.ELEMENT_KEY);
        this.factory = factory;
        this.symbol = symbol;
    }

    public ElementBuilder<T, P> properties(NonNullUnaryOperator<Element.Properties> func) {
        propertiesCallback = propertiesCallback.andThen(func);
        return this;
    }

    public ElementBuilder<T, P> initialProperties(NonNullSupplier<Element.Properties> properties) {
        initialProperties = properties;
        return this;
    }

    public ElementBuilder<T, P> defaultLang() {
        return lang(Element::getDescriptionId);
    }

    public ElementBuilder<T, P> lang(String name) {
        return lang(Element::getDescriptionId, name);
    }

    @Override
    protected @NonnullType T createEntry() {
        Element.Properties properties = this.initialProperties.get();
        properties = propertiesCallback.apply(properties);
        return factory.apply(properties.symbol(symbol));
    }

    @Override
    protected RegistryEntry<T> createEntryWrapper(RegistryObject<T> delegate) {
        return new ElementEntry<>(getOwner(), delegate);
    }

    @Override
    public ElementEntry<T> register() {
        RutileAPI.registerElement(new ResourceLocation(getOwner().getModid(), getName()), createEntry());
        return (ElementEntry<T>) super.register();
    }
}
