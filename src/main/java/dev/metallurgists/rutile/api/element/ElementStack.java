package dev.metallurgists.rutile.api.element;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.metallurgists.rutile.RutileClient;
import dev.metallurgists.rutile.api.RutileApi;
import dev.metallurgists.rutile.api.data.ISerializable;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.resources.ResourceLocation;

public class ElementStack implements ISerializable {
    public static final Codec<ElementStack> CODEC;
    public static final Codec<ElementStack> SINGLE_CODEC;
    @Getter @Setter
    private int amount;
    @Getter
    private final ResourceLocation id;

    public ElementStack(ResourceLocation id) {
        this.id = id;
    }

    public ElementStack(ResourceLocation id, int amount) {
        this.id = id;
        this.amount = amount;
    }

    public static ElementStack of(ResourceLocation id) {
        return new ElementStack(id, 1);
    }

    public static ElementStack of(ResourceLocation id, int amount) {
        return new ElementStack(id, amount);
    }

    public static ElementStack of(Element element) {
        return new ElementStack(element.getId(), 1);
    }

    public static ElementStack of(Element element, int amount) {
        return new ElementStack(element.getId(), amount);
    }

    public Element getElement() {
        return RutileApi.getElementRegistry().getById(id);
    }

    public ElementStack copy() {
        return new ElementStack(this.getId(), this.amount);
    }

    public String getDisplay() {
        StringBuilder display = new StringBuilder(getElement().getSymbol());
        if (amount > 1)
            display.append(amount);
        return RutileClient.toSmallDownNumbers(display.toString());
    }

    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ResourceLocation.CODEC.fieldOf("id").forGetter(ElementStack::getId),
                Codec.INT.fieldOf("amount").forGetter(ElementStack::getAmount)
        ).apply(instance, ElementStack::new));
        SINGLE_CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ResourceLocation.CODEC.fieldOf("id").forGetter(ElementStack::getId)
        ).apply(instance, ElementStack::new));
    }

    @Override
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        serialize(ResourceLocation.CODEC, this.id, "id", json);
        serialize(Codec.INT, this.amount, "amount", json);
        return json;
    }
}
