package dev.metallurgists.rutile.api.material.module;

import dev.metallurgists.rutile.api.material.module.registry.RegistryModule;
import net.minecraft.core.Holder;

import java.util.Collection;
import java.util.Map;

public interface RedirectModule<R, T extends RedirectModule<R, T>> extends MaterialModule<T> {

    Map<Holder<RegistryModule.Key>, Collection<R>> getRedirects();
}
