package dev.metallurgists.rutile.api.data.provider.composition;

import com.mojang.serialization.Codec;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import dev.metallurgists.rutile.api.composition.Composition;
import dev.metallurgists.rutile.api.composition.SubComposition;
import dev.metallurgists.rutile.api.composition.content.HolderContent;
import dev.metallurgists.rutile.api.composition.element.ElementStack;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.WithConditions;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public abstract class AbstractCompositionProvider<T> implements DataProvider {
    private final PackOutput.PathProvider pathProvider;
    private final CompletableFuture<HolderLookup.Provider> registries;
    private final String modId;
    private final String type;
    private final Map<String, Builder<T>> dataBuilders = new LinkedHashMap<>();

    public AbstractCompositionProvider(String modId, String type, PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        this.modId = modId;
        this.type = type;
        this.pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "rutile/composition/" + type);
        this.registries = registries;
    }

    protected abstract ResourceKey<? extends Registry<T>> registryKey();

    protected abstract Registry<T> registry();

    protected Codec<T> entryCodec() {
        return registry().byNameCodec();
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
            this.dataBuilders.forEach((name, builder) -> {
                Path path = this.pathProvider.json(ResourceLocation.fromNamespaceAndPath(this.modId, name));
                list.add(DataProvider.saveStable(pOutput, pProv, Composition.conditionalCodec(registryKey(), entryCodec()), Optional.of(new WithConditions<>(builder.conditions(), builder.composition())), path));
            });
            return CompletableFuture.allOf(list.toArray(CompletableFuture[]::new));
        });
    }

    public final Builder<T> addData(String name, HolderContent<T> content, Composition<T> composition, ICondition... conditions) {
        Composition<T> complete = new Composition<>(content, composition.compositions());
        Builder<T> builder = new Builder<>(complete, Arrays.asList(conditions));
        this.dataBuilders.putIfAbsent(name, builder);
        return builder;
    }

    public final Builder<T> addData(String name, HolderContent<T> content, NonNullFunction<Composition.Builder<T>, Composition.Builder<T>> composition, ICondition... conditions) {
        return addData(name, content, composition.apply(Composition.<T>builder()).end(), conditions);
    }

    public final Builder<T> addData(String name, T value, Composition<T> composition, ICondition... conditions) {
        return addData(name, HolderContent.of(List.of(value)), composition, conditions);
    }

    public final Builder<T> addData(String name, T value, NonNullFunction<Composition.Builder<T>, Composition.Builder<T>> composition, ICondition... conditions) {
        return addData(name, HolderContent.of(List.of(value)), composition, conditions);
    }

    public final Builder<T> addData(String name, TagKey<T> tag, Composition<T> composition, ICondition... conditions) {
        return addData(name, HolderContent.of(tag), composition, conditions);
    }

    public final Builder<T> addData(String name, TagKey<T> tag, NonNullFunction<Composition.Builder<T>, Composition.Builder<T>> composition, ICondition... conditions) {
        return addData(name, HolderContent.of(tag), composition, conditions);
    }

    public final Builder<T> addTags(String name, List<TagKey<T>> tags, Composition<T> composition, ICondition... conditions) {
        return addData(name, HolderContent.of(List.of(), tags), composition, conditions);
    }

    public final Builder<T> addTags(String name, List<TagKey<T>> tags, NonNullFunction<Composition.Builder<T>, Composition.Builder<T>> composition, ICondition... conditions) {
        return addTags(name, tags, composition.apply(Composition.<T>builder()).end(), conditions);
    }

    public final Builder<T> addData(String name, List<? extends T> values, List<TagKey<T>> tags, Composition<T> composition, ICondition... conditions) {
        return addData(name, HolderContent.of(values, tags), composition, conditions);
    }

    public final Builder<T> addData(String name, List<? extends T> values, List<TagKey<T>> tags, NonNullFunction<Composition.Builder<T>, Composition.Builder<T>> composition, ICondition... conditions) {
        return addData(name, values, tags, composition.apply(Composition.<T>builder()).end(), conditions);
    }

    public final Builder<T> addData(String name, List<? extends T> values, Composition<T> composition, ICondition... conditions) {
        List<T> contents = new ArrayList<>(values);
        return addData(name, HolderContent.of(contents), composition, conditions);
    }

    public final Builder<T> addData(String name, List<? extends T> values, NonNullFunction<Composition.Builder<T>, Composition.Builder<T>> composition, ICondition... conditions) {
        return addData(name, values, composition.apply(Composition.<T>builder()).end(), conditions);
    }

    /**
     * Starts a {@link HolderContent} that can mix direct entries and tags, e.g.
     * {@code holder().add(item).add(tag)}.
     */
    protected HolderContent<T> holder() {
        return new HolderContent<>();
    }

    ResourceLocation getKey(T value) {
        return registry().getKey(value);
    }

    @Nonnull
    public final String getName() {
        String uppercaseType = StringUtils.capitalize(this.type);
        return uppercaseType + " Compositions for " + this.modId;
    }

    public Composition<T> composition(ElementStack... elements) {
        SubComposition subComposition = subComposition(elements);
        return new Composition<>( new LinkedList<>(Collections.singletonList(subComposition)));
    }

    public Composition<T> composition(SubComposition... subCompositions) {
        return new Composition<>(new LinkedList<>(Arrays.asList(subCompositions)));
    }

    public SubComposition subComposition(ElementStack... elements) {
        return SubComposition.builder().element(elements).build();
    }

    public record Builder<T>(@NotNull Composition<T> composition, List<ICondition> conditions) {

    }
}