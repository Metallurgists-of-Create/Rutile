package dev.metallurgists.rutile.registry;

import dev.metallurgists.rutile.api.material.module.MaterialModule;
import dev.metallurgists.rutile.api.material.module.types.registry.NameAlternativeModule;
import dev.metallurgists.rutile.api.material.module.types.runtime.TooltipModule;
import dev.metallurgists.rutile.api.material.module.types.runtime.UnitSizeModule;
import dev.metallurgists.rutile.api.registry.RutileRegistries;

import java.util.function.Supplier;

public class RutileModules {

    public static final NameAlternativeModule NAME_ALTERNATIVE = create("name_alternative", NameAlternativeModule::new);

    public static final UnitSizeModule UNIT_SIZE = create("unit_size", UnitSizeModule::new);

    public static final TooltipModule TOOLTIP = create("tooltip", () -> TooltipModule.INSTANCE);

    public static void init() {}

    public static <T extends MaterialModule<?>> T create(String name, Supplier<T> supplier) {
        return RutileRegistries.register(RutileRegistries.MODULES, name, supplier.get());
    }
}
