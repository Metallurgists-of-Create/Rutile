package dev.metallurgists.rutile.registry;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.registry.deferred.DeferredModules;
import dev.metallurgists.rutile.api.material.module.MaterialModule;
import dev.metallurgists.rutile.registry.deferred.ModuleHolder;
import dev.metallurgists.rutile.api.material.module.types.registry.NameAlternativeModule;
import dev.metallurgists.rutile.api.material.module.types.runtime.TooltipModule;
import dev.metallurgists.rutile.api.material.module.types.runtime.UnitSizeModule;
import dev.metallurgists.rutile.util.RegistryHelper;

import java.util.function.Supplier;

public class RutileModules {
    public static final DeferredModules MODULES = RegistryHelper.createModules(Rutile.ID);

    public static final ModuleHolder<NameAlternativeModule> NAME_ALTERNATIVE = create("name_alternative", NameAlternativeModule::new);

    public static final ModuleHolder<UnitSizeModule> UNIT_SIZE = create("unit_size", UnitSizeModule::new);

    public static final ModuleHolder<TooltipModule> TOOLTIP = create("tooltip", () -> TooltipModule.INSTANCE);

    public static void init() {}

    public static <T extends MaterialModule<?>> ModuleHolder<T> create(String name, Supplier<T> supplier) {
        return MODULES.register(name, supplier);
    }
}
