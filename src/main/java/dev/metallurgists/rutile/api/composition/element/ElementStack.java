package dev.metallurgists.rutile.api.composition.element;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.metallurgists.rutile.api.data.ISerializable;
import dev.metallurgists.rutile.config.RutileConfig;
import dev.metallurgists.rutile.registry.RutileRegistries;
import dev.metallurgists.rutile.util.StringFormatUtil;
import lombok.Getter;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ElementStack implements ISerializable {
    @Getter
    private final Element element;
    @Getter
    private final int amount;
    @Getter
    private final double mass;

    public ElementStack(Element element, int amount, double mass) {
        this.element = element;
        this.amount = amount;
        this.mass = mass == -1 ? element.getMass() : mass;
    }

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

    public ResourceLocation getId() {
        Element element = getElement();
        return element != null ? element.getId() : Element.NULL.getId();
    }

    public int getColor() {
        Element element = getElement();
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
        StringBuilder display = new StringBuilder(getElement().getSymbol());
        if (amount > 1)
            display.append(amount);
        return StringFormatUtil.toSmallDownNumbers(display.toString());
    }

    //TODO: Find a way to stop the codec from printing the default mass.
    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group(
                RutileRegistries.ELEMENTS_REGISTRY.byNameCodec().fieldOf("id").forGetter(ElementStack::getElement),
                Codec.INT.fieldOf("amount").forGetter(ElementStack::getAmount),
                Codec.DOUBLE.optionalFieldOf("mass", -1.0d).forGetter(ElementStack::getMass)
        ).apply(instance, ElementStack::new));
        SINGLE_CODEC = RecordCodecBuilder.create(instance -> instance.group(
                RutileRegistries.ELEMENTS_REGISTRY.byNameCodec().fieldOf("id").forGetter(ElementStack::getElement)
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
        return o instanceof ElementStack stack
                && this.amount == stack.getAmount()
                && this.mass == stack.getMass()
                && this.element.equals(stack.getElement());
    }

    @Override
    public String toString() {
        return this.element.getSymbol() + " x" + this.amount;
    }

    public void getTooltip(Consumer<Component> tooltip, TooltipFlag flag) {
        boolean advanced = flag.isAdvanced();
        if (advanced) {
            tooltip.accept((Component.literal(getId().toString())).withStyle(ChatFormatting.DARK_GRAY));
        }
        String displayedMass = (advanced || !RutileConfig.getClient().roundMass.get()) ? "" + mass : String.format("%.2f", mass);
        tooltip.accept((Component.literal(displayedMass + "amu")).withStyle(ChatFormatting.YELLOW));
    }
}
