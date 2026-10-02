package dev.metallurgists.rutile.compat.kubejs.event;

import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;

public interface RutileKubeEvents {
    EventGroup GROUP = EventGroup.of("Rutile");

    EventHandler REGISTER_ELEMENTS = GROUP.startup("registerElements", () -> KubeElementBuilder.class);
}
