package dev.metallurgists.rutile.api.registrate.builder;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.metallurgists.rutile.api.material.base.Material;
import net.neoforged.neoforge.registries.DeferredHolder;

public class MaterialEntry<T extends Material> extends RegistryEntry<Material, T> {

    public MaterialEntry(AbstractRegistrate<?> owner, DeferredHolder<Material, T> delegate) {
        super(owner, delegate);
    }

    public static <T extends Material> MaterialEntry<T> cast(RegistryEntry<Material, T> entry) {
        return RegistryEntry.cast(MaterialEntry.class, entry);
    }
}
