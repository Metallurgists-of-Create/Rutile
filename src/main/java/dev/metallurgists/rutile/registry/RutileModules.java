package dev.metallurgists.rutile.registry;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.material.module.*;
import dev.metallurgists.rutile.api.material.module.dynamic.SableModule;
import dev.metallurgists.rutile.api.material.module.dynamic.TagsModule;
import dev.metallurgists.rutile.api.material.module.registry.FluidModule;
import dev.metallurgists.rutile.api.material.module.registry.MetalModule;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import java.util.function.Supplier;

public class RutileModules {
    public static ResourceKey<Registry<MaterialModule<?>>> MODULES_KEY = ResourceKey.createRegistryKey(Rutile.id("material_modules"));
    public static final DeferredModules MODULES = DeferredModules.create(Rutile.ID);
    public static final Registry<MaterialModule<?>> MODULES_REGISTRY = MODULES.makeRegistry(builder -> builder.sync(true)
            .defaultKey(Rutile.id("composition")));

    public static final ModuleHolder<CompositionModule> COMPOSITION = create("composition", () -> CompositionModule.INSTANCE);
    public static final ModuleHolder<NameAlternativeModule> NAME_ALTERNATIVE = create("name_alternative", NameAlternativeModule::new);
    public static final ModuleHolder<IgnoreModule> IGNORE = create("ignore", IgnoreModule::new);
    public static final ModuleHolder<ItemRedirectModule> ITEM_REDIRECT = create("item_redirect", ItemRedirectModule::new);
    public static final ModuleHolder<TooltipModule> TOOLTIP = create("tooltip", () -> TooltipModule.INSTANCE);
    public static final ModuleHolder<BlockAssetsModule> BLOCK_ASSETS = create("block_assets", BlockAssetsModule::new);
    public static final ModuleHolder<UnitSizeModule> UNIT_SIZE = create("unit_size", UnitSizeModule::new);
    public static final ModuleHolder<SecondaryMaterialsModule> SECONDARY_MATERIALS = create("secondary_materials", SecondaryMaterialsModule::new);
    public static final ModuleHolder<TagsModule> TAGS = create("tags", TagsModule::new);


    // Compat Modules
    public static final ModuleHolder<SableModule> SABLE = create("sable", SableModule::new);

    // Registry Modules
    public static final ModuleHolder<FluidModule> FLUID = create("fluid", FluidModule::new);
    public static final ModuleHolder<MetalModule> METAL = create("metal", MetalModule::new);


    public static <T extends MaterialModule<?>> ModuleHolder<T> create(String name, Supplier<T> supplier) {
        return MODULES.register(name, supplier);
    }
}
