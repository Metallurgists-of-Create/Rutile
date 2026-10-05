package dev.metallurgists.rutile.compat.kubejs.data;

import dev.latvian.mods.kubejs.error.KubeRuntimeException;
import dev.latvian.mods.kubejs.event.KubeEvent;
import dev.latvian.mods.rhino.util.HideFromJS;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import dev.metallurgists.rutile.api.composition.Composition;
import dev.metallurgists.rutile.api.composition.SubComposition;
import dev.metallurgists.rutile.api.composition.content.HolderContent;
import dev.metallurgists.rutile.api.data.manager.composition.AbstractCompositionManager;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import java.util.*;
import java.util.function.Consumer;

public class KubeCompositionBuilder<T> implements KubeEvent {
    private final transient AbstractCompositionManager<T> manager;

    private final transient List<Builder> builders = new ArrayList<>();

    @HideFromJS
    public KubeCompositionBuilder(AbstractCompositionManager<T> manager) {
        this.manager = manager;
    }

    @HideFromJS
    public AbstractCompositionManager<T> getManager() {
        return this.manager;
    }

    @HideFromJS
    public Map<String, Composition<T>> getCompositions() {
        Map<String, Composition<T>> compositions = new LinkedHashMap<>();
        for (Builder builder : this.builders) {
            compositions.put(builder.name, builder.build());
        }
        return compositions;
    }

    public Builder create() {
        return this.create("kube_" + UUID.randomUUID());
    }

    public Builder create(String name) {
        if (name == null || name.isBlank()) {
            throw new KubeRuntimeException("A composition name can't be blank");
        }

        if (name.contains("/") || name.contains(".")) {
            throw new KubeRuntimeException("'%s' is not a valid composition name".formatted(name));
        }

        for (Builder existing : this.builders) {
            if (existing.name.equals(name)) {
                throw new KubeRuntimeException(
                        "A composition named '%s' has already been declared for %s compositions");
            }
        }

        Builder builder = new Builder(name);
        this.builders.add(builder);
        return builder;
    }

    @HideFromJS
    public Registry<T> getRegistry() {
        return this.manager.getRegistry();
    }

    public class Builder {
        private final String name;
        private final HolderContent<T> holderContent = new HolderContent<>();
        private final LinkedList<SubComposition> subCompositions = new LinkedList<>();
        private KubeSubCompositionBuilder current = new KubeSubCompositionBuilder(this::next);

        private Builder(String name) {
            this.name = name;
        }

        @HideFromJS
        public Builder add(T entry) {
            this.holderContent.add(entry);
            return this;
        }

        @ReturnsSelf
        public Builder add(CompositionEntry entry) {
            KubeCompositionBuilder.this.addEntry(this.holderContent, entry);
            return this;
        }

        @ReturnsSelf
        public Builder elements(Consumer<KubeSubCompositionBuilder> consumer) {
            consumer.accept(this.current);
            return this;
        }

        @HideFromJS
        public KubeSubCompositionBuilder next() {
            this.commit();
            this.current = new KubeSubCompositionBuilder(this::next);
            return this.current;
        }

        private void commit() {
            SubComposition built = this.current.build();
            if (!built.getElements().isEmpty()) {
                this.subCompositions.add(built);
            }
        }

        @HideFromJS
        public Composition<T> build() {
            LinkedList<SubComposition> compositions = new LinkedList<>(this.subCompositions);
            SubComposition last = this.current.build();

            if (!last.getElements().isEmpty()) {
                compositions.add(last);
            }

            return new Composition<>(this.holderContent, compositions);
        }
    }

    @HideFromJS
    public String registryId() {
        return this.manager.getRegistryKey().location().toString();
    }

    @HideFromJS
    void addEntry(HolderContent<T> content, CompositionEntry entry) {
        switch (entry) {
            case CompositionEntry.Tag tag -> content.add(tagKey(tag.id()));
            case CompositionEntry.Single single -> content.add(registryEntry(single.id()));
        }
    }

    @HideFromJS
    T registryEntry(ResourceLocation id) {
        Registry<T> registry = this.manager.getRegistry();

        if (!registry.containsKey(id)) {
            throw new KubeRuntimeException("No entry with id '%s' is registered in '%s'".formatted(id, registryId()));
        }

        return registry.get(id);
    }

    @HideFromJS
    public TagKey<T> tagKey(ResourceLocation id) {
        return TagKey.create(castRegistryKey(this.manager.getRegistryKey()), id);
    }

    @SuppressWarnings("unchecked")
    private static <T> ResourceKey<Registry<T>> castRegistryKey(ResourceKey<? extends Registry<T>> registryKey) {
        return (ResourceKey<Registry<T>>) registryKey;
    }
}