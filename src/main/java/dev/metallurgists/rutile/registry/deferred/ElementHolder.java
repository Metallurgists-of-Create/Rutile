package dev.metallurgists.rutile.registry.deferred;

import dev.metallurgists.rutile.api.element.Element;
import dev.metallurgists.rutile.api.element.ElementLike;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ElementHolder<T extends Element> extends DeferredHolder<Element, T> implements ElementLike {
    protected ElementHolder(ResourceKey<Element> key) {
        super(key);
    }

    @Override
    public Element asElement() {
        return this.get();
    }
}
