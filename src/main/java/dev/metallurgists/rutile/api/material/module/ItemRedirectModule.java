package dev.metallurgists.rutile.api.material.module;

import dev.metallurgists.rutile.api.material.module.registry.RegistryModule;
import dev.metallurgists.rutile.registry.RutileModules;
import net.minecraft.core.Holder;
import net.minecraft.world.level.ItemLike;

import java.util.*;
import java.util.function.Supplier;

public class ItemRedirectModule implements RedirectModule<Supplier<? extends ItemLike>, ItemRedirectModule> {
    private final Map<Holder<RegistryModule.Key>, Collection<Supplier<? extends ItemLike>>> redirects;

    public ItemRedirectModule() {
        this.redirects = new HashMap<>();
    }

    @Override
    public Map<Holder<RegistryModule.Key>, Collection<Supplier<? extends ItemLike>>> getRedirects() {
        return redirects;
    }

    @SafeVarargs
    public final ItemRedirectModule redirect(Holder<RegistryModule.Key> key, Supplier<? extends ItemLike>... items) {
        redirects.computeIfPresent(key, (k, i) -> {
            i.addAll(Arrays.asList(items));
            return i;
        });
        redirects.computeIfAbsent(key, (k) -> Arrays.asList(items));
        return this;
    }

    @Override
    public ModuleHolder<ItemRedirectModule> getType() {
        return RutileModules.ITEM_REDIRECT;
    }

    @Override
    public ModuleBuilder<ItemRedirectModule> builder() {
        return new Builder();
    }

    public static class Builder implements ModuleBuilder<ItemRedirectModule> {
        private final Map<Holder<RegistryModule.Key>, Collection<Supplier<? extends ItemLike>>> redirects = new HashMap<>();

        @SafeVarargs
        public final Builder redirect(Holder<RegistryModule.Key> key, Supplier<? extends ItemLike>... items) {
            redirects.computeIfPresent(key, (k, i) -> {
                i.addAll(Arrays.asList(items));
                return i;
            });
            redirects.computeIfAbsent(key, (k) -> Arrays.asList(items));
            return this;
        }

        @Override
        public ItemRedirectModule build() {
            ItemRedirectModule module = new ItemRedirectModule();
            module.redirects.putAll(redirects);
            return module;
        }
    }
}
