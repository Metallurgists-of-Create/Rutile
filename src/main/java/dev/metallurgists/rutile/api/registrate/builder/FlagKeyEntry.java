package dev.metallurgists.rutile.api.registrate.builder;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.metallurgists.rutile.api.material.flag.FlagKey;
import dev.metallurgists.rutile.api.material.flag.IMaterialFlag;
import net.neoforged.neoforge.registries.DeferredHolder;

public class FlagKeyEntry<T extends FlagKey<? extends IMaterialFlag>> extends RegistryEntry<FlagKey<? extends IMaterialFlag>, T> {

    public FlagKeyEntry(AbstractRegistrate<?> owner, DeferredHolder<FlagKey<? extends IMaterialFlag>, T> delegate) {
        super(owner, delegate);
    }

    public static <T extends FlagKey<? extends IMaterialFlag>> FlagKeyEntry<T> cast(RegistryEntry<FlagKey<? extends IMaterialFlag>, T> entry) {
        return RegistryEntry.cast(FlagKeyEntry.class, entry);
    }
}
