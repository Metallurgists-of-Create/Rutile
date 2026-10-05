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
import org.jetbrains.annotations.Nullable;

public interface ElementStackWrapper {
    @HideFromJS
    static ElementStack wrapElementStack(Context cx, Object from) {
        return switch (unwrap(from)) {
            case null -> ElementStack.NULL;
            case ElementStack s -> s;
            case Element e -> e.isNull() ? ElementStack.NULL : e.asStack();
            case ElementLike l -> l.asElement().isNull() ? ElementStack.NULL : l.asStack();
            case ResourceLocation id -> require(cx, id).asStack();
            case CharSequence s -> parse(cx, s.toString());
            default -> throw new KubeRuntimeException("Failed to read element stack %s".formatted(from))
                    .source(SourceLine.of(cx));
        };
    }

    static ElementStack of(Element element) {
        return element.asStack();
    }

    static ElementStack of(Element element, int amount) {
        return element.asStack(amount);
    }


    static ElementStack of(ElementStack stack, double mass) {
        return new ElementStack(stack.getElement(), stack.getAmount(), mass);
    }

    static ElementStack copyWithAmount(ElementStack stack, int amount) {
        return stack.copyWithAmount(amount);
    }

    static ElementStack grow(ElementStack stack, int amount) {
        return stack.grow(amount);
    }

    static ElementStack shrink(ElementStack stack, int amount) {
        return stack.shrink(amount);
    }

    static ElementStack getEmpty() {
        return ElementStack.NULL;
    }

    @HideFromJS
    private static ElementStack parse(Context cx, String string) {
        String trimmed = string.trim();
        int amount = 1;
        int separator = trimmed.lastIndexOf(' ');

        if (separator > 0) {
            String tail = trimmed.substring(separator + 1).trim();
            if (!tail.isEmpty()) {
                try {
                    amount = Integer.parseInt(tail);
                } catch (NumberFormatException ignored) {
                    separator = -1;
                }
            }
        }

        String id = separator > 0 ? trimmed.substring(0, separator).trim() : trimmed;

        ResourceLocation parsed = ResourceLocation.tryParse(id);
        if (parsed == null) {
            throw new KubeRuntimeException("'%s' is not a valid element id".formatted(id))
                    .source(SourceLine.of(cx));
        }

        return new ElementStack(require(cx, parsed), amount);
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

    @HideFromJS
    private static @Nullable Object unwrap(@Nullable Object from) {
        Object value = from;
        while (value instanceof Wrapper wrapper) {
            value = wrapper.unwrap();
        }
        return value;
    }
}