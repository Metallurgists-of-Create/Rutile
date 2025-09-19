package dev.metallurgists.rutile.api.registrate.builder;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.metallurgists.rutile.api.composition.element.Element;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ElementEntry<T extends Element> extends RegistryEntry<Element, T> {

    public ElementEntry(AbstractRegistrate<?> owner, DeferredHolder<Element, T> delegate) {
        super(owner, delegate);
    }

    public static <T extends Element> ElementEntry<T> cast(RegistryEntry<Element, T> entry) {
        return RegistryEntry.cast(ElementEntry.class, entry);
    }
}
