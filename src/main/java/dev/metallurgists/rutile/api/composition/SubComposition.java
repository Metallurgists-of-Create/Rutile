package dev.metallurgists.rutile.api.composition;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.metallurgists.rutile.api.data.ISerializable;
import dev.metallurgists.rutile.api.element.ElementStack;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SubComposition implements ISerializable {
    public static final Codec<SubComposition> CODEC;

    @Getter
    @Setter
    private int amount = 1;
    @Getter
    private final List<ElementStack> elements;

    public SubComposition(List<ElementStack> elements) {
        this.elements = elements;
    }

    public SubComposition(List<ElementStack> elements, int amount) {
        this.elements = elements;
        this.amount = amount;
    }

    public ElementStack getElement(int index) {
        return elements.get(index);
    }

    public static List<SubComposition> createComposition(SubComposition.Builder... subCompositionBuilders) {
        List<SubComposition> subCompositions = new ArrayList<>();
        for (SubComposition.Builder builder : subCompositionBuilders) {
            subCompositions.add(builder.build());
        }
        return subCompositions;
    }

    public static List<SubComposition> createFromList(List<ElementStack> elementStacks) {
        List<SubComposition> subCompositions = new ArrayList<>();
        for (ElementStack elementStack : elementStacks) {
            subCompositions.add(new SubComposition(List.of(elementStack), 1));
        }
        return subCompositions;
    }

    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.list(ElementStack.CODEC).fieldOf("elements").forGetter(SubComposition::getElements),
                Codec.INT.optionalFieldOf("amount", 1).forGetter(SubComposition::getAmount)
        ).apply(instance, SubComposition::new));
    }

    @Override
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        serialize(Codec.INT, getAmount(), "amount", json);
        serialize(ElementStack.CODEC.listOf(), elements, "elements", json);
        return json;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final List<ElementStack> elements = new ArrayList<>();
        private int amount = 1;

        public Builder() {}

        public Builder element(ElementStack element) {
            elements.add(element);
            return this;
        }
        public Builder element(ElementStack... elements) {
            this.elements.addAll(Arrays.stream(elements).toList());
            return this;
        }

        public Builder setAmount(int amount) {
            this.amount = amount;
            return this;
        }

        public SubComposition build() {
            return new SubComposition(elements, amount);
        }
    }
}
