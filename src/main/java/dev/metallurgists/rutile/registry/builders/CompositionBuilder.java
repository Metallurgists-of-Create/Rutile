package dev.metallurgists.rutile.registry.builders;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import dev.metallurgists.rutile.api.composition.Composition;
import dev.metallurgists.rutile.api.composition.SubComposition;
import dev.metallurgists.rutile.api.element.ElementLike;
import dev.metallurgists.rutile.api.element.ElementStack;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CompositionBuilder<R> extends AbstractBuilder<Object, Composition, R, CompositionBuilder<R>> {

    private final List<SubComposition> subCompositions = new ArrayList<>();
    private SubComposition.Builder currentComposition;

    public CompositionBuilder(AbstractRegistrate<?> owner, R parent, String name, BuilderCallback callback, ResourceKey<? extends Registry<Object>> registryKey) {
        super(owner, parent, name, callback, registryKey);
        this.currentComposition = SubComposition.builder();
    }

    public CompositionBuilder<R> element(ElementStack element) {
        this.currentComposition.element(element);
        return this;
    }

    public CompositionBuilder<R> element(ElementLike element) {
        this.currentComposition.element(element);
        return this;
    }

    public CompositionBuilder<R> element(ElementLike element, int amount) {
        this.currentComposition.element(element, amount);
        return this;
    }

    public CompositionBuilder<R> element(ElementStack... elements) {
        this.currentComposition.element(elements);
        return this;
    }

    public CompositionBuilder<R> setAmount(int amount) {
        this.currentComposition.setAmount(amount);
        return this;
    }

    public CompositionBuilder<R> next() {
        this.subCompositions.add(currentComposition.build());
        this.currentComposition = SubComposition.builder();
        return this;
    }

    @Override
    protected @NotNull Composition createEntry() {
        this.subCompositions.add(currentComposition.build());
        return new Composition(subCompositions);
    }
}
