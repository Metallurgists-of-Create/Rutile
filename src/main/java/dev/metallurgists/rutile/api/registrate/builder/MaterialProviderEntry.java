package dev.metallurgists.rutile.api.registrate.builder;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.base.MaterialLike;
import dev.metallurgists.rutile.api.material.base.MaterialStack;
import net.neoforged.neoforge.registries.DeferredHolder;

public class MaterialProviderEntry<R extends MaterialLike, T extends R> extends RegistryEntry<R, T> implements MaterialLike {
    public MaterialProviderEntry(AbstractRegistrate<?> owner, DeferredHolder<R, T> delegate) {
        super(owner, delegate);
    }

    public MaterialStack asStack() {
        return new MaterialStack(asMaterial(), 1);
    }

    public MaterialStack asStack(int count) {
        return new MaterialStack(asMaterial(), count);
    }

    public boolean isIn(MaterialStack stack) {
        return is(stack.material());
    }

    public boolean is(Material material) {
        return asMaterial() == material;
    }

    @Override
    public Material asMaterial() {
        return get().asMaterial();
    }
}
