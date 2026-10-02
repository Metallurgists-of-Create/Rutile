package dev.metallurgists.rutile.compat.kubejs.registry;

import dev.latvian.mods.kubejs.event.KubeEvent;
import dev.latvian.mods.kubejs.util.KubeResourceLocation;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;

import java.util.LinkedList;

public class KubeElementBuilder implements KubeEvent {
    @Getter
    private final LinkedList<Built> builtElements = new LinkedList<>();

    public void create(KubeResourceLocation id, String symbol, int color) {
        int argb = (color & 0xFF000000) == 0 ? (0xFF000000 | color) : color;
        this.builtElements.add(new Built(id.wrapped(), symbol, argb));
    }

    public record Built(ResourceLocation id, String symbol, int color) {}
}
