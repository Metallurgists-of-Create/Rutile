package dev.metallurgists.rutile.compat.kubejs.wrapper;

import dev.latvian.mods.kubejs.error.KubeRuntimeException;
import dev.latvian.mods.kubejs.script.SourceLine;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.Wrapper;
import dev.latvian.mods.rhino.util.HideFromJS;
import dev.metallurgists.rutile.api.composition.element.Element;
import dev.metallurgists.rutile.api.composition.element.ElementLike;
import dev.metallurgists.rutile.api.composition.element.ElementStack;
import dev.metallurgists.rutile.registry.RutileElements;
import net.minecraft.resources.ResourceLocation;

public interface ElementWrapper {
    @HideFromJS
    static Element wrapElement(Context cx, Object from) {
        while (from instanceof Wrapper wrapper) {
            from = wrapper.unwrap();
        }

        return switch (from) {
            case null -> RutileElements.NULL.asElement();
            case Element e -> e;
            case ElementLike l -> l.asElement();
            case ResourceLocation id -> require(cx, id);
            case CharSequence s -> require(cx, parse(cx, s.toString()));
            default -> throw new KubeRuntimeException("Failed to read element %s".formatted(from))
                    .source(SourceLine.of(cx));
        };
    }

    static boolean exists(ResourceLocation id) {
        return RutileElements.get(id) != null;
    }

    static ElementStack of(Element element) {
        return element.asStack();
    }

    static boolean isNull(Element element) {
        return element.isNull();
    }

    @HideFromJS
    private static ResourceLocation parse(Context cx, String id) {
        ResourceLocation parsed = ResourceLocation.tryParse(id);
        if (parsed == null) {
            throw new KubeRuntimeException("'%s' is not a valid element id".formatted(id)).source(SourceLine.of(cx));
        }
        return parsed;
    }

    @HideFromJS
    private static Element require(Context cx, ResourceLocation id) {
        Element element = RutileElements.get(id);
        if (element == null) {
            throw new KubeRuntimeException("Element with id %s does not exist!".formatted(id))
                    .source(SourceLine.of(cx));
        }
        return element;
    }

}