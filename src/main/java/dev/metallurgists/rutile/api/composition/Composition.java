package dev.metallurgists.rutile.api.composition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.metallurgists.rutile.RutileClient;
import dev.metallurgists.rutile.api.composition.content.HolderContent;
import dev.metallurgists.rutile.api.composition.element.ElementLike;
import dev.metallurgists.rutile.api.composition.element.ElementStack;
import lombok.experimental.Accessors;
import net.createmod.catnip.utility.lang.LangBuilder;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.common.conditions.ConditionalOps;
import net.neoforged.neoforge.common.conditions.WithConditions;

import java.util.*;

@Accessors(chain = true, fluent = true)
public record Composition<T>(LinkedList<SubComposition> compositions, HolderContent<T> contents) {
    public static final Composition<?> EMPTY = new Composition<>(new LinkedList<>());

    public Composition(LinkedList<SubComposition> compositions) {
        this(compositions, null);
    }

    public Composition(HolderContent<T> contents, LinkedList<SubComposition> compositions) {
        this(compositions, contents);
    }

    // Needed for codec
    private Composition(HolderContent<T> content, List<SubComposition> subCompositions) {
        this(content, new LinkedList<>(subCompositions));
    }

    /**
     * Creates a {@link Codec} for a {@link Composition} that applies to the registry entries
     * described by a {@link HolderContent}.
     */
    public static <T> Codec<Composition<T>> codec(ResourceKey<? extends Registry<T>> registryKey, Codec<T> entryCodec) {
        return RecordCodecBuilder.create(inst -> inst.group(
                HolderContent.codec(registryKey, entryCodec).fieldOf("contents").forGetter(Composition::contents),
                SubComposition.CODEC.listOf().fieldOf("compositions").forGetter(Composition::compositions)
        ).apply(inst, Composition::new));
    }

    public static <T> Codec<Optional<WithConditions<Composition<T>>>> conditionalCodec(ResourceKey<? extends Registry<T>> registryKey, Codec<T> entryCodec) {
        return ConditionalOps.createConditionalCodecWithConditions(codec(registryKey, entryCodec));
    }

    public Composition<T> add(SubComposition subComposition) {
        this.compositions.add(subComposition);
        return this;
    }

    public boolean isEmpty() {
        return this == EMPTY;
    }

    public boolean shouldHaveBrackets() {
        return compositions().size() > 1;
    }

    public String getString() {
        StringBuilder sb = new StringBuilder();
        for (SubComposition subComposition : compositions()) {
            if (subComposition == null) continue;
            LangBuilder subComp = RutileClient.getLang();
            int subCompAmount = compositions().size();
            boolean encaseInBrackets = subCompAmount > 1;
            if (encaseInBrackets) subComp.add(Component.literal("("));
            for (int j = 0; j < subComposition.getElements().size(); j++) {
                if (subComposition.getElements().get(j) == null) continue;
                ElementStack elementStack = subComposition.getElements().get(j);
                subComp.add(Component.literal(elementStack.getDisplay()));
            }
            if (encaseInBrackets) subComp.add(Component.literal(")"));
            sb.append(subComp.string());
        }
        return sb.toString();
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public boolean isSameComposition(Composition<T> composition) {
        return this.compositions.equals(composition.compositions);
    }

    public static class Builder<T> {
        private final LinkedList<SubComposition> subCompositions = new LinkedList<>();

        private SubComposition.Builder currentComposition;

        public Builder() {
            this.currentComposition = SubComposition.builder();
        }

        public Builder<T> element(ElementStack element) {
            this.currentComposition.element(element);
            return this;
        }

        public Builder<T> element(ElementLike element) {
            this.currentComposition.element(element);
            return this;
        }

        public Builder<T> element(ElementLike element, int amount) {
            this.currentComposition.element(element, amount);
            return this;
        }

        public Builder<T> element(double mass, ElementLike element, int amount) {
            this.currentComposition.element(mass, element, amount);
            return this;
        }

        public Builder<T> element(ElementStack... elements) {
            this.currentComposition.element(elements);
            return this;
        }

        public Builder<T> setAmount(int amount) {
            this.currentComposition.setAmount(amount);
            return this;
        }

        public Builder<T> next() {
            this.subCompositions.add(currentComposition.build());
            this.currentComposition = SubComposition.builder();
            return this;
        }

        public Composition<T> end() {
            this.subCompositions.add(currentComposition.build());
            return new Composition<>(subCompositions);
        }
    }
}
