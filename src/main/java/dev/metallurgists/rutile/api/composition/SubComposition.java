package dev.metallurgists.rutile.api.composition;

import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import dev.metallurgists.rutile.RutileClient;
import dev.metallurgists.rutile.api.composition.element.ElementLike;
import dev.metallurgists.rutile.api.composition.element.ElementStack;
import dev.metallurgists.rutile.config.RutileConfig;
import dev.metallurgists.rutile.util.ColourUtil;
import dev.metallurgists.rutile.util.StringFormatUtil;
import lombok.Getter;
import lombok.Setter;
import net.createmod.catnip.utility.lang.LangBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

import java.util.*;

public class SubComposition implements IComposable {
    public static final Codec<SubComposition> CODEC;

    @Getter
    @Setter
    private int amount = 1;
    @Getter
	private final List<IComposable> elements;

    public SubComposition(List<IComposable> elements) {
		this.elements = elements;
    }

    public SubComposition(List<IComposable> elements, int amount) {
        this.elements = elements;
        this.amount = amount;
    }

    public int getColor() {
		List<Integer> colours = new ArrayList<>(elements.stream().map(IComposable::getColor).toList());
        return ColourUtil.blendAll(colours);
    }

    public Style groupStyle() {
		int color = RutileConfig.getClient().elementColorForTooltip.get() ? getColor() : RutileConfig.getClient().tooltipColor.get();
		return Style.EMPTY.withColor(color);
    }

    public LangBuilder getDisplay() {
        LangBuilder builder = RutileClient.getLang();
        boolean encaseInBrackets = getAmount() > 1 || elements.size() > 1;
        if (encaseInBrackets) builder.add(Component.literal("(").setStyle(groupStyle()));
		for (IComposable comp : elements) { builder.add(comp.getDisplay()); }
        if (encaseInBrackets) builder.add(Component.literal(")").setStyle(groupStyle()));
        if (getAmount() > 1) builder.add(Component.literal(StringFormatUtil.toSmallDownNumbers(String.valueOf(getAmount()))).setStyle(groupStyle()));
        return builder;
    }

    public HashMap<ElementStack, Integer> getContainedElements() {
		return elements.stream().map(IComposable::getContainedElements).reduce((one, two) -> {
			for (HashMap.Entry<ElementStack, Integer> entry : two.entrySet())
				one.merge(entry.getKey(), entry.getValue(), Integer::sum);
			return one;
		}).orElse(new HashMap<>());
    }

    public int getContainedAmount() {
        return getContainedElements().values().stream().mapToInt(Integer::intValue).sum();
    }


    /* TODO: Is this needed? If so, fix it.
	public ElementStack getElement(int index) {
        return elements.get(index);
    }*/

    public static List<SubComposition> createComposition(SubComposition.Builder... subCompositionBuilders) {
        List<SubComposition> subCompositions = new ArrayList<>();
        for (SubComposition.Builder builder : subCompositionBuilders) {
            subCompositions.add(builder.build());
        }
        return subCompositions;
    }

    public static List<SubComposition> createFromList(List<IComposable> elementStacks) {
        List<SubComposition> subCompositions = new ArrayList<>();
        for (IComposable elementStack : elementStacks) {
            subCompositions.add(new SubComposition(List.of(elementStack), 1));
        }
        return subCompositions;
    }

    static {
        CODEC = Codec.recursive(SubComposition.class.getSimpleName(),
			recursed -> RecordCodecBuilder.create(instance -> instance.group(
					Codec.list(Codec.xor(ElementStack.CODEC, recursed).flatComapMap(
						(e) -> e.map(ee -> (IComposable) ee, ee -> (IComposable) ee),
						(c) -> {
							Either<ElementStack, SubComposition> either = null;
							if (c instanceof ElementStack cee) either = Either.left(cee);
							else if (c instanceof SubComposition csc) either = Either.right(csc);
							if (either == null) return DataResult.error(() -> "Failed to parse composition elements.");
							return DataResult.success(either);
						}
					)).fieldOf("elements").forGetter(SubComposition::getElements),
					Codec.INT.optionalFieldOf("amount", 1).forGetter(SubComposition::getAmount)
                ).apply(instance, SubComposition::new)
			)
		);
    }

    @Override
    public JsonObject toJson() {
        return serialize(CODEC, this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final List<IComposable> elements = new ArrayList<>();
        private int amount = 1;

        public Builder() {}

        public Builder nested(NonNullFunction<Builder, Builder> nest) {
			elements.add(nest.apply(SubComposition.builder()).build());
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
            return new SubComposition(elements, amount);
        }
    }
}
