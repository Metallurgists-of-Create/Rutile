package dev.metallurgists.rutile.events;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;

public class RutileEventHandler {
    private final IEventBus modEventBus;

    public RutileEventHandler(IEventBus modEventBus) {
        this.modEventBus = modEventBus;
    }

    public void register() {
        registerModEvents(modEventBus);
        registerForgeEvents(NeoForge.EVENT_BUS);
    }

    private void registerForgeEvents(IEventBus eventBus) {

    }

    private void registerModEvents(IEventBus eventBus) {
        eventBus.register(new RegistryEvents());
    }

}
