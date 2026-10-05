package dev.metallurgists.rutile.compat.kubejs.data;

import net.minecraft.resources.ResourceLocation;

public sealed interface CompositionEntry permits CompositionEntry.Single, CompositionEntry.Tag {
    ResourceLocation id();

    record Single(ResourceLocation id) implements CompositionEntry {}

    record Tag(ResourceLocation id) implements CompositionEntry {}
}