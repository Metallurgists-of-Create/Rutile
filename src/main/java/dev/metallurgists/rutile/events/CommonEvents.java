package dev.metallurgists.rutile.events;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.fluid.MaterialBucketItem;
import dev.metallurgists.rutile.api.registry.PostRutileRegistryEvent;
import dev.metallurgists.rutile.api.registry.RutileRegistries;
import dev.metallurgists.rutile.api.registry.RutileRegistry;
import dev.metallurgists.rutile.api.runtime.RutilePackSource;
import dev.metallurgists.rutile.api.runtime.assets.RutileDynamicResourcePack;
import dev.metallurgists.rutile.api.runtime.data.RutileDynamicDataPack;
import dev.metallurgists.rutile.debug.material.DebugElementPrinter;
import dev.metallurgists.rutile.debug.material.DebugMaterialPrinter;
import dev.metallurgists.rutile.debug.material.DebugTagPrefixPrinter;
import dev.metallurgists.rutile.registry.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoader;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.fluids.capability.wrappers.FluidBucketWrapper;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

public class CommonEvents {

    private static IEventBus modBus;

    public static void init(final IEventBus modBus) {
        CommonEvents.modBus = modBus;
        modBus.register(CommonEvents.class);

        RutileRegistries.init(modBus);
        Rutile.registrate.registerEventListeners(modBus);
    }

    // Only register everything once.
    private static boolean didRunRegistration = false;

    @SubscribeEvent
    public static void onRegister(RegisterEvent event) {
        if (didRunRegistration) {
            return;
        }
        RutileElements.init();
        RutileMaterials.init();

        RutileIngredientTypes.ITEM_INGREDIENT_TYPES.register(modBus);
        didRunRegistration = true;
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onRegisterEarly(RegisterEvent event) {
        if (event.getRegistryKey() == RutileRegistries.MATERIAL_REGISTRY) {
            RutileRegistries.MATERIALS.close();
            ModLoader.postEventWrapContainerInModOrder(PostRutileRegistryEvent.MATERIAL);
        }
        registerMaterials(event);
    }

    private static void registerMaterials(RegisterEvent event) {
        event.register(Registries.BLOCK, registry -> {

        });
        event.register(Registries.ITEM, registry -> {
            // Items are registered along with blocks in the new system
        });
        event.register(Registries.FLUID, registry -> {

        });
    }


    @SubscribeEvent
    public static void loadComplete(FMLLoadCompleteEvent event) {
        if (Rutile.isDev()) {
            DebugTagPrefixPrinter.print();
            DebugMaterialPrinter.print();
            DebugElementPrinter.print();
        }
        RutileRegistries.getRutileRegistries().forEach(RutileRegistry::onLoadComplete);
    }

    @SubscribeEvent
    public static void registerRegistries(NewRegistryEvent event) {
        RutileRegistries.getRegistries().forEach(event::register);
    }

    @SubscribeEvent
    public static void addPackFinders(AddPackFindersEvent event) {
        if (event.getPackType() == PackType.CLIENT_RESOURCES) {
            RutileDynamicResourcePack.clearClient();

            event.addRepositorySource(new RutilePackSource("rutile:dynamic_assets",
                    event.getPackType(),
                    Pack.Position.BOTTOM,
                    RutileDynamicResourcePack::new));
        } else if (event.getPackType() == PackType.SERVER_DATA) {
            RutileDynamicDataPack.clearServer();

            event.addRepositorySource(new RutilePackSource("rutile:dynamic_data",
                    event.getPackType(),
                    Pack.Position.BOTTOM,
                    RutileDynamicDataPack::new));
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        for (Item item : BuiltInRegistries.ITEM) {
            if (item instanceof MaterialBucketItem) {
                event.registerItem(Capabilities.FluidHandler.ITEM,
                        (stack, ctx) -> new FluidBucketWrapper(stack), item);
            }
        }
    }
}
