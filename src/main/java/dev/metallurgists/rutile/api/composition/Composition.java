package dev.metallurgists.rutile.api.composition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.metallurgists.rutile.RutileClient;
import dev.metallurgists.rutile.api.element.ElementLike;
import dev.metallurgists.rutile.api.element.ElementStack;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.createmod.catnip.lang.LangBuilder;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.common.conditions.ConditionalOps;
import net.neoforged.neoforge.common.conditions.WithConditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Accessors(chain = true, fluent = true)
public class Composition {
    @Getter
    public List<SubComposition> compositions;

    public static final Composition EMPTY = new Composition(List.of());

    public static final Codec<Composition> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            SubComposition.CODEC.listOf().fieldOf("compositions").forGetter(Composition::compositions)
    ).apply(inst, Composition::new));

    public Composition(List<SubComposition> compositions) {
        this.compositions = compositions;
    }

    public Composition add(SubComposition subComposition) {
        this.compositions.add(subComposition);
        return this;
    }

    public boolean isEmpty() {
        return this == EMPTY;
    }

    public boolean shouldHaveBrackets() {
        return compositions().size() > 1;
    }

    public static final Codec<Optional<WithConditions<Composition>>> CONDITIONAL_CODEC =
            ConditionalOps.createConditionalCodecWithConditions(CODEC);

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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final List<SubComposition> subCompositions = new ArrayList<>();

        private SubComposition.Builder currentComposition;

        public Builder() {
            this.currentComposition = SubComposition.builder();
        }

        public Builder element(ElementStack element) {
            this.currentComposition.element(element);
            return this;
        }

        public Builder element(ElementLike element) {
            this.currentComposition.element(element);
            return this;
        }

        public Builder element(ElementLike element, int amount) {
            this.currentComposition.element(element, amount);
            return this;
        }

        public Builder element(ElementStack... elements) {
            this.currentComposition.element(elements);
            return this;
        }

        public Builder setAmount(int amount) {
            this.currentComposition.setAmount(amount);
            return this;
        }

        public Builder next() {
            this.subCompositions.add(currentComposition.build());
            this.currentComposition = SubComposition.builder();
            return this;
        }

        public Composition end() {
            this.subCompositions.add(currentComposition.build());
            return new Composition(subCompositions);
        }
    }
}
