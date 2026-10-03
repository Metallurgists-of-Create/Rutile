package dev.metallurgists.rutile.api.composition.element;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.metallurgists.rutile.api.data.ISerializable;
import dev.metallurgists.rutile.registry.RutileElements;
import dev.metallurgists.rutile.util.StringFormatUtil;
import net.minecraft.resources.ResourceLocation;

public record ElementStack(int amount, ResourceLocation id) implements ISerializable {
    public static final ElementStack NULL = new ElementStack(Element.NULL, 1);

    public static final Codec<ElementStack> CODEC;
    public static final Codec<ElementStack> SINGLE_CODEC;

    public ElementStack(ElementLike element, int amount) {
        this(amount, element.asElement().getId());
    }

    public ElementStack(ElementLike element) {
        this(element.asElement().getId(), 1);
    }

    public ElementStack(ResourceLocation id) {
        this(id, 1);
    }

    public ElementStack(ResourceLocation id, int amount) {
        this(amount, id);
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
        return RutileElements.get(id);
    }

    public int getColor() {
        Element element = getElement();
        return element != null ? element.getColor() : 0xFFFFFFFF;
    }

    public ElementStack copy() {
        return new ElementStack(this.id(), this.amount);
    }

    public ElementStack copyWithAmount(int amount) {
        return amount == this.amount ? this : new ElementStack(this.id, amount);
    }

    public ElementStack grow(int amount) {
        return this.copyWithAmount(this.amount + amount);
    }

    public ElementStack shrink(int amount) {
        return this.copyWithAmount(Math.max(0, this.amount - amount));
    }

    public ElementStack copyWithElement(ElementLike element) {
        return new ElementStack(element, this.amount);
    }

    public String getDisplay() {
        StringBuilder display = new StringBuilder(getElement().getSymbol());
        if (amount > 1)
            display.append(amount);
        return StringFormatUtil.toSmallDownNumbers(display.toString());
    }

    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ResourceLocation.CODEC.fieldOf("id").forGetter(ElementStack::id),
                Codec.INT.fieldOf("amount").forGetter(ElementStack::amount)
        ).apply(instance, ElementStack::new));
        SINGLE_CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ResourceLocation.CODEC.fieldOf("id").forGetter(ElementStack::id)
        ).apply(instance, ElementStack::new));
    }

    @Override
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        serialize(ResourceLocation.CODEC, this.id, "id", json);
        serialize(Codec.INT, this.amount, "amount", json);
        return json;
    }

    public boolean isNull() {
        return this == NULL;
    }

    public boolean isEmpty() {
        return this.amount <= 0;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof ElementStack(int amount1, ResourceLocation id1)
                && this.amount == amount1
                && this.id.equals(id1);
    }

    @Override
    public String toString() {
        return this.id + " x" + this.amount;
    }
}
