package dev.metallurgists.rutile.api.composition.element;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.metallurgists.rutile.api.registry.RutileAPI;
import dev.metallurgists.rutile.registry.ElementRegistry;
import net.minecraft.resources.ResourceLocation;

public record ElementStack(ResourceLocation id) {
    public static final Codec<ElementStack> CODEC;

    public static ElementStack of(ResourceLocation id) {
        return new ElementStack(id);
    }

    public static ElementStack of(Element element) {
        return new ElementStack(element.getId());
    }

    public Element getElement() {
        return RutileAPI.getElementRegistry().getById(id);
    }

    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ResourceLocation.CODEC.fieldOf("id").forGetter(ElementStack::id)
        ).apply(instance, ElementStack::new));
    }
}
