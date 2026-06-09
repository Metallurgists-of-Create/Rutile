package dev.metallurgists.rutile.api.material.module.types.runtime;

import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.material.module.MaterialModule;
import dev.metallurgists.rutile.registry.deferred.ModuleHolder;
import dev.metallurgists.rutile.registry.RutileModules;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.function.BiConsumer;

public record TooltipModule(BiConsumer<Material, List<Component>> tooltip) implements MaterialModule<TooltipModule> {
    public static final TooltipModule INSTANCE = new TooltipModule(null);

    @Override
    public ModuleHolder<TooltipModule> getType() {
        return RutileModules.TOOLTIP;
    }

    @Override
    public ModuleBuilder<TooltipModule> builder() {
        return () -> TooltipModule.INSTANCE;
    }
}
