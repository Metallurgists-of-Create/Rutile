package dev.metallurgists.rutile.api.registrate.material;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.metallurgists.rutile.api.material.base.Material;
import net.minecraftforge.registries.RegistryObject;

public class MaterialEntry<T extends Material> extends RegistryEntry<T> {
    public MaterialEntry(AbstractRegistrate<?> owner, RegistryObject<T> delegate) {
        super(owner, delegate);
    }

    public static <T extends Material> MaterialEntry<T> cast(RegistryEntry<T> entry) {
        return RegistryEntry.cast(MaterialEntry.class, entry);
    }
}
