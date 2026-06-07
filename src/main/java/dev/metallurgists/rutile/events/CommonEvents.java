package dev.metallurgists.rutile.events;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.registry.RutileRegistries;
import dev.metallurgists.rutile.api.registry.RutileRegistry;
import dev.metallurgists.rutile.debug.material.DebugElementPrinter;
import dev.metallurgists.rutile.registry.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
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
        RutileModules.init();

        RutileIngredientTypes.ITEM_INGREDIENT_TYPES.register(modBus);
        didRunRegistration = true;
    }


    @SubscribeEvent
    public static void loadComplete(FMLLoadCompleteEvent event) {
        if (Rutile.isDev()) {
            DebugElementPrinter.print();
        }
        RutileRegistries.getRutileRegistries().forEach(RutileRegistry::onLoadComplete);
    }

    @SubscribeEvent
    public static void registerRegistries(NewRegistryEvent event) {
        RutileRegistries.getRegistries().forEach(event::register);
    }
}
