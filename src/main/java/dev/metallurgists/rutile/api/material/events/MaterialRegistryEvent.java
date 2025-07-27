package dev.metallurgists.rutile.api.material.events;

import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;

public class MaterialRegistryEvent extends Event implements IModBusEvent {

    public MaterialRegistryEvent() {
        super();
    }
}
