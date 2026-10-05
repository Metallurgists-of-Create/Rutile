package dev.metallurgists.rutile.api.composition;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import dev.metallurgists.rutile.RutileClient;
import dev.metallurgists.rutile.api.composition.element.ElementLike;
import dev.metallurgists.rutile.api.composition.element.ElementStack;
import dev.metallurgists.rutile.api.data.ISerializable;
import dev.metallurgists.rutile.config.RutileConfig;
import dev.metallurgists.rutile.util.ColourUtil;
import dev.metallurgists.rutile.util.StringFormatUtil;
import lombok.Getter;
import lombok.Setter;
import net.createmod.catnip.utility.lang.LangBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

import java.util.*;

public class SubComposition implements ISerializable {
    public static final Codec<SubComposition> CODEC;

    @Getter
    @Setter
    private int amount = 1;
    @Getter
    private final List<ElementStack> elements;
    @Getter
    private final List<SubComposition> nested;

    public SubComposition(List<ElementStack> elements) {
        this.elements = elements;
        this.nested = List.of();
    }

    public SubComposition(List<ElementStack> elements, int amount, List<SubComposition> nested) {
        this.elements = elements;
        this.amount = amount;
        this.nested = nested;
    }

    public int getColor() {
        List<Integer> colours = new ArrayList<>();
        if (!getElements().isEmpty()) {
            colours.addAll(getElements().stream().map(ElementStack::getColor).toList());
        }
        if (!getNested().isEmpty()) {
            colours.addAll(getNested().stream().map(SubComposition::getColor).toList());
        }
        return ColourUtil.blendAll(colours);
    }

    public Style groupStyle() {
        var style = Style.EMPTY.withColor(getColor());
        if (!RutileConfig.getClient().elementColorForTooltip.get()) {
            style = Style.EMPTY.withColor(RutileConfig.getClient().tooltipColor.get());
        }
        return style;
    }

    public LangBuilder getDisplay() {
        LangBuilder builder = RutileClient.getLang();
        boolean encaseInBrackets = getAmount() > 1;
        if (encaseInBrackets) builder.add(Component.literal("(").setStyle(groupStyle()));
        for (int j = 0; j < getElements().size(); j++) {
            if (getElements().get(j) == null) continue;
            ElementStack elementStack = getElements().get(j);
            MutableComponent elementComp = Component.literal(elementStack.getDisplay());
            if (RutileConfig.getClient().elementColorForTooltip.get()) {
                elementComp = elementComp.setStyle(Style.EMPTY.withColor(elementStack.getColor()));
            }
            builder.add(elementComp);
        }
        for (int j = 0; j < getNested().size(); j++) {
            SubComposition nested = getNested().get(j);
            builder.add(Component.literal("(").setStyle(nested.groupStyle()));
            builder.add(nested.getDisplay());
            builder.add(Component.literal(")").setStyle(nested.groupStyle()));
        }
        if (encaseInBrackets) builder.add(Component.literal(")").setStyle(groupStyle()));
        if (getAmount() > 1) builder.add(Component.literal(StringFormatUtil.toSmallDownNumbers(String.valueOf(getAmount()))).setStyle(groupStyle()));
        return builder;
    }

    public Map<ElementStack, Integer> getContainedElements() {
        Map<ElementStack, Integer> elements = new HashMap<>();
        for (ElementStack element : getElements()) {
            ElementStack single = element.copyWithAmount(1);
            int amount = element.getAmount();
            elements.put(single, elements.getOrDefault(single, 0) + amount);
        }
        for (SubComposition nested : getNested()) {
            for (Map.Entry<ElementStack, Integer> entry : nested.getContainedElements().entrySet()) {
                ElementStack single = entry.getKey().copyWithAmount(1);
                int amount = entry.getValue();
                elements.put(single, elements.getOrDefault(single, 0) + amount);
            }
        }
        return elements;
    }

    public int getContainedAmount() {
        return getContainedElements().values().stream().mapToInt(Integer::intValue).sum();
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
            subCompositions.add(new SubComposition(List.of(elementStack), 1, List.of()));
        }
        return subCompositions;
    }

    static {
        CODEC = Codec.recursive(SubComposition.class.getSimpleName(), recursed ->
                RecordCodecBuilder.create(instance -> instance.group(
                        Codec.list(ElementStack.CODEC).optionalFieldOf("elements", List.of()).forGetter(SubComposition::getElements),
                        Codec.INT.optionalFieldOf("amount", 1).forGetter(SubComposition::getAmount),
                        recursed.listOf().optionalFieldOf("nested", List.of()).forGetter(SubComposition::getNested)
                ).apply(instance, SubComposition::new)));
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
        private final List<SubComposition> nested = new ArrayList<>();
        private int amount = 1;

        public Builder() {}

        public Builder nested(NonNullFunction<Builder, Builder> nest) {
            nested.add(nest.apply(SubComposition.builder()).build());
            return this;
        }

        public Builder element(ElementStack element) {
            elements.add(element);
            return this;
        }

        public Builder element(ElementLike element) {
            elements.add(element.asStack());
            return this;
        }

        public Builder element(ElementLike element, int amount) {
            elements.add(element.asStack(amount));
            return this;
        }

        public Builder element(double mass, ElementLike element, int amount) {
            elements.add(element.asStack(mass, amount));
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
            return new SubComposition(elements, amount, nested);
        }
    }
}
