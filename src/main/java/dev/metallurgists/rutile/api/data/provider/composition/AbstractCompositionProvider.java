package dev.metallurgists.rutile.api.data.provider.composition;

import com.tterrag.registrate.util.nullness.NonNullFunction;
import dev.metallurgists.rutile.api.composition.Composition;
import dev.metallurgists.rutile.api.composition.SubComposition;
import dev.metallurgists.rutile.api.composition.element.ElementStack;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.WithConditions;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public abstract class AbstractCompositionProvider<T> implements DataProvider {
    private final PackOutput.PathProvider pathProvider;
    private final CompletableFuture<HolderLookup.Provider> registries;
    private final String modId;
    private final String type;
    private final Map<T, Builder> dataBuilders = new HashMap<>();

    public AbstractCompositionProvider(String modId, String type, PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        this.modId = modId;
        this.type = type;
        this.pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "rutile/composition/" + type);
        this.registries = registries;
    }

    public void generate(HolderLookup.Provider registries) {}

    @Override
    public final CompletableFuture<?> run(CachedOutput pOutput) {
        return this.registries.thenCompose(pProv -> this.run(pOutput, pProv));
    }

    public CompletableFuture<?> run(CachedOutput pOutput, HolderLookup.Provider pProv) {
        return this.registries.thenCompose((provider) -> {
            List<CompletableFuture<?>> list = new ArrayList<>();
            this.generate(provider);
            this.dataBuilders.forEach((value, builder) -> {
                Path path = this.pathProvider.json(getKey(value));
                list.add(DataProvider.saveStable(pOutput, pProv, Composition.CONDITIONAL_CODEC, Optional.of(new WithConditions<>(builder.conditions(), builder.composition())), path));
            });
            return CompletableFuture.allOf(list.toArray(CompletableFuture[]::new));
        });
    }

    public final Builder addData(T value, Composition composition, ICondition... conditions) {
        return this.dataBuilders.computeIfAbsent(value,
                (h) -> new Builder(composition, Arrays.asList(conditions)));
    }

    public final Builder addData(T value, NonNullFunction<Composition.Builder, Composition.Builder> composition, ICondition... conditions) {
        return this.dataBuilders.computeIfAbsent(value,
                (h) -> new Builder(composition.apply(new Composition.Builder()).end(), Arrays.asList(conditions)));
    }

    public final void addData(List<? extends T> values, NonNullFunction<Composition.Builder, Composition.Builder> composition, ICondition... conditions) {
        values.forEach(value -> addData(value, composition, conditions));
    }

    abstract ResourceLocation getKey(T value);

    @Nonnull
    public final String getName() {
        String uppercaseType = StringUtils.capitalize(this.type);
        return uppercaseType + " Compositions for " + this.modId;
    }

    public Composition composition(ElementStack... elements) {
        SubComposition subComposition = subComposition(elements);
        return new Composition(List.of(subComposition));
    }

    public Composition composition(SubComposition... subCompositions) {
        return new Composition(List.of(subCompositions));
    }

    public SubComposition subComposition(ElementStack... elements) {
        return SubComposition.builder().element(elements).build();
    }

    public record Builder(@NotNull Composition composition, List<ICondition> conditions) {

    }
}
