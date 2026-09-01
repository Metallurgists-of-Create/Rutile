package dev.metallurgists.rutile.events;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.registry.RutileElements;
import dev.metallurgists.rutile.registry.RutileRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

public class CommonEvents {

    private static IEventBus modBus;

    public static void init(final IEventBus modBus) {
        CommonEvents.modBus = modBus;
        modBus.register(CommonEvents.class);

        Rutile.getRegistrate().registerEventListeners(modBus);
    }

    // Only register everything once.
    private static boolean didRunRegistration = false;

    @SubscribeEvent
    public static void onRegister(RegisterEvent event) {
        if (didRunRegistration) {
            return;
        }
        RutileElements.init();

        didRunRegistration = true;
    }
}
