package dev.metallurgists.rutile.compat.kubejs.data;

import dev.latvian.mods.kubejs.error.KubeRuntimeException;
import dev.latvian.mods.kubejs.script.SourceLine;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.Wrapper;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

public interface CompositionEntryWrapper {
    @HideFromJS
    static CompositionEntry wrapEntry(Context cx, Object from) {
        while (from instanceof Wrapper wrapper) {
            from = wrapper.unwrap();
        }

        String id;

        switch (from) {
            case CompositionEntry entry -> {
                return entry;
            }
            case TagKey<?> tag -> {
                return new CompositionEntry.Tag(tag.location());
            }
            case ResourceLocation location -> id = location.toString();
            case CharSequence sequence -> id = sequence.toString();
            case null, default -> throw new KubeRuntimeException("Failed to read composition entry %s".formatted(from))
                    .source(SourceLine.of(cx));
        }

        boolean tag = id.startsWith("#");
        if (tag) {
            id = id.substring(1);
        }

        ResourceLocation parsed = ResourceLocation.tryParse(id.trim());

        if (parsed == null) {
            throw new KubeRuntimeException("'%s' is not a valid composition entry id".formatted(id)).source(SourceLine.of(cx));
        }

        return tag ? new CompositionEntry.Tag(parsed) : new CompositionEntry.Single(parsed);
    }
}