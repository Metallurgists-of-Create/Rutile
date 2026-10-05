package dev.metallurgists.rutile.compat.kubejs.event;

import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;
import dev.latvian.mods.kubejs.event.EventTargetType;
import dev.latvian.mods.kubejs.event.TargetedEventHandler;
import dev.metallurgists.rutile.compat.kubejs.data.KubeCompositionBuilder;
import dev.metallurgists.rutile.compat.kubejs.registry.KubeElementBuilder;
import net.minecraft.resources.ResourceLocation;

public interface RutileKubeEvents {
    EventGroup GROUP = EventGroup.of("Rutile");

    EventHandler REGISTER_ELEMENTS = GROUP.startup("registerElements", () -> KubeElementBuilder.class);
    TargetedEventHandler<ResourceLocation> COMPOSITION = GROUP.server("compositions", () -> KubeCompositionBuilder.class).requiredTarget(EventTargetType.ID);
}
