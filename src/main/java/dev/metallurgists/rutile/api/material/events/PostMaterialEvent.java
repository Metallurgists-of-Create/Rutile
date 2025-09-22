package dev.metallurgists.rutile.api.material.events;

import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;

public class PostMaterialEvent extends Event implements IModBusEvent {

    public PostMaterialEvent() {
        super();
    }
}
