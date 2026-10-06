package dev.metallurgists.rutile.events;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.composition.RutileCompositions;
import dev.metallurgists.rutile.api.plugin.PluginRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

public class CommonEvents {
    public static void init(final IEventBus modBus) {
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
        PluginRegistry.getInstance().callRegistration();
        RutileCompositions.INSTANCE.collectManagers();

        didRunRegistration = true;
    }
}
