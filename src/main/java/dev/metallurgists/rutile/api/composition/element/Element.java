package dev.metallurgists.rutile.api.composition.element;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.metallurgists.rutile.api.registry.CustomRutileRegistries;
import dev.metallurgists.rutile.registry.RutileRegistries;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceLocation;

public record Element(String symbol, int color) {
    public static final Codec<Element> DIRECT_CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.STRING.optionalFieldOf("symbol", "?").forGetter(Element::symbol),
            Codec.INT.optionalFieldOf("color", 0x818181).forGetter(Element::color)
    ).apply(instance, Element::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, Element> DIRECT_STREAM_CODEC;
    public static final Codec<Holder<Element>> CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<Element>> STREAM_CODEC;

    static {
        DIRECT_STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8, Element::symbol,
                ByteBufCodecs.INT, Element::color,
                Element::new);
        CODEC = RegistryFileCodec.create(CustomRutileRegistries.ELEMENT_REGISTRY, DIRECT_CODEC);
        STREAM_CODEC = ByteBufCodecs.holder(CustomRutileRegistries.ELEMENT_REGISTRY, DIRECT_STREAM_CODEC);
    }

    public Element(String symbol, int color) {
        this.symbol = symbol;
        this.color = color;
    }

    public ResourceLocation getId() {
        return CustomRutileRegistries.ELEMENTS.getKey(this);
    }

    public Component getDisplayName() {
        String key = getId().toLanguageKey("element");
        return Component.translatable(key);
    }
}
