package dev.metallurgists.rutile.compat.kubejs.data;

import dev.latvian.mods.rhino.util.ReturnsSelf;
import dev.metallurgists.rutile.api.composition.SubComposition;
import dev.metallurgists.rutile.api.composition.element.ElementStack;

import java.util.function.Supplier;

public class KubeSubCompositionBuilder {
    private final transient SubComposition.Builder delegate = SubComposition.builder();
    private final transient Supplier<KubeSubCompositionBuilder> next;

    KubeSubCompositionBuilder(Supplier<KubeSubCompositionBuilder> next) {
        this.next = next;
    }

    @ReturnsSelf
    public KubeSubCompositionBuilder element(ElementStack stack) {
        this.delegate.element(stack);
        return this;
    }

    @ReturnsSelf
    public KubeSubCompositionBuilder element(ElementStack stack, int amount) {
        this.delegate.element(stack.copyWithAmount(amount));
        return this;
    }

    @ReturnsSelf
    public KubeSubCompositionBuilder elements(ElementStack... stacks) {
        this.delegate.element(stacks);
        return this;
    }

    @ReturnsSelf
    public KubeSubCompositionBuilder amount(int amount) {
        this.delegate.setAmount(amount);
        return this;
    }

    public KubeSubCompositionBuilder next() {
        return this.next.get();
    }

    SubComposition build() {
        return this.delegate.build();
    }
}