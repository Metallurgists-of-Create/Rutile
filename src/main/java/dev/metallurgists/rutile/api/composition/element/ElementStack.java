package dev.metallurgists.rutile.api.composition.element;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.metallurgists.rutile.api.data.ISerializable;
import dev.metallurgists.rutile.registry.RutileRegistries;
import dev.metallurgists.rutile.util.StringFormatUtil;

public record ElementStack(Element element, int amount, double mass) implements ISerializable {
    public static final ElementStack NULL = new ElementStack(Element.NULL, 1);

    public static final Codec<ElementStack> CODEC;
    public static final Codec<ElementStack> SINGLE_CODEC;

    public ElementStack(ElementLike element, int amount) {
        this(element.asElement(), amount, element.asElement().getMass());
    }

    public ElementStack(ElementLike element) {
        this(element.asElement(), 1, element.asElement().getMass());
    }

    public static ElementStack of(Element element) {
        return new ElementStack(element, 1, element.asElement().getMass());
    }

    public static ElementStack of(Element element, int amount) {
        return new ElementStack(element, amount, element.asElement().getMass());
    }

    public Element getElement() {
        return element;
    }

    public int getColor() {
        Element element = element();
        return element != null ? element.getColor() : 0xFFFFFFFF;
    }

    public ElementStack copy() {
        return new ElementStack(this.element, this.amount, this.mass);
    }

    public ElementStack copyWithAmount(int amount) {
        return amount == this.amount ? this : new ElementStack(this.element, amount, this.mass);
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
        StringBuilder display = new StringBuilder(element().getSymbol());
        if (amount > 1)
            display.append(amount);
        return StringFormatUtil.toSmallDownNumbers(display.toString());
    }

    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group(
                RutileRegistries.ELEMENTS_REGISTRY.byNameCodec().fieldOf("id").forGetter(ElementStack::element),
                Codec.INT.fieldOf("amount").forGetter(ElementStack::amount),
                Codec.DOUBLE.optionalFieldOf("mass", element.getMass()).forGetter(ElementStack::mass)
        ).apply(instance, ElementStack::new));
        SINGLE_CODEC = RecordCodecBuilder.create(instance -> instance.group(
                RutileRegistries.ELEMENTS_REGISTRY.byNameCodec().fieldOf("id").forGetter(ElementStack::element)
        ).apply(instance, ElementStack::new));
    }

    @Override
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        serialize(RutileRegistries.ELEMENTS_REGISTRY.byNameCodec(), this.element, "id", json);
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
        return o instanceof ElementStack(Element element1, int amount1, double mass1)
                && this.amount == amount1
                && this.mass == mass1
                && this.element.equals(element1);
    }

    @Override
    public String toString() {
        return this.element.getSymbol() + " x" + this.amount;
    }
}
