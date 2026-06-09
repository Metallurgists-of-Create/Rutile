package dev.metallurgists.rutile.api.material;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.metallurgists.rutile.Rutile;
import net.neoforged.neoforge.registries.DeferredHolder;

public class MaterialEntry extends RegistryEntry<Material, Material> {
    public MaterialEntry(AbstractRegistrate owner, DeferredHolder<Material, Material> key) {
        super(owner, key);
    }

    public static MaterialEntry cast(RegistryEntry<Material, Material> entry) {
        return new MaterialEntry(Rutile.getRegistrate(), entry);
    }
}
