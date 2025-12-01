package dev.metallurgists.rutile.events;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.RutileApi;
import dev.metallurgists.rutile.api.data.ItemCompositionManager;
import dev.metallurgists.rutile.api.data.MaterialCompositionManager;
import dev.metallurgists.rutile.api.registry.ElementRegistry;
import dev.metallurgists.rutile.api.registry.MaterialRegistry;
import dev.metallurgists.rutile.debug.material.DebugMaterialPrinter;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

public class CommonEvents {

    private static IEventBus modBus;

    public static void init(final IEventBus modBus) {
        CommonEvents.modBus = modBus;
        ModLoadingContext modLoadingContext = ModLoadingContext.get();

        modBus.register(CommonEvents.class);
        Rutile.registrate.registerEventListeners(modBus);
    }

    // Only register everything once.
    private static boolean didRunRegistration = false;

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onRegisterEarly(RegisterEvent event) {
        if (didRunRegistration) {
            return;
        }
        MaterialRegistry.getInstance().setAllowRegistration(true);
        MaterialRegistry.getInstance().onRegisterObjects(event);
        MaterialRegistry.getInstance().setAllowRegistration(false);
        ElementRegistry.getInstance().onRegisterObjects(event);
        didRunRegistration = true;
    }



    @SubscribeEvent
    public static void loadComplete(FMLLoadCompleteEvent event) {
        if (!FMLEnvironment.production) {
            DebugMaterialPrinter.print();
        }
        MaterialRegistry.getInstance().onLoadComplete();
        ElementRegistry.getInstance().onLoadComplete();
    }
}
