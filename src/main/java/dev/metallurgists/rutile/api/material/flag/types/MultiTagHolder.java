package dev.metallurgists.rutile.api.material.flag.types;

import dev.metallurgists.rutile.api.material.base.Material;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class MultiTagHolder {

    protected List<HolderEntry<?>> holders = new ArrayList<>();

    public <T> MultiTagHolder(TagHolder<T> holder) {
        holders.add(HolderEntry.create(holder.tagPath, holder));
    }

    public <T, T1> MultiTagHolder(TagHolder<T> holder, TagHolder<T1> holder1) {
        holders.add(HolderEntry.create(holder.tagPath, holder));
        holders.add(HolderEntry.create(holder1.tagPath, holder1));
    }

    public <T> void addHolder(ResourceKey<Registry<T>> registry) {
        holders.add(HolderEntry.create(registry, new TagHolder<>(registry)));
    }

    public <T> void addHolder(TagHolder<T> holder) {
        holders.add(HolderEntry.create(holder.tagPath, holder));
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public <T> TagHolder<T> getHolder(ResourceKey<Registry<T>> registry) {
        return (TagHolder<T>) holders.stream().filter(ent -> ent.test(registry)).map(HolderEntry::holder).findFirst().orElse(null);
    }

    public <T> boolean has(ResourceKey<Registry<T>> registry) {
        return !holders.stream().filter(ent -> ent.test(registry)).toList().isEmpty();
    }

    public <T> void addPatterns(ResourceKey<Registry<T>> registry, String... patterns) {
        var holder = getHolder(registry);
        if (holder == null) {
            TagHolder<T> tagHolder = new TagHolder<>(registry);
            tagHolder.addPatterns(patterns);
            addHolder(tagHolder);
        } else holder.addPatterns(patterns);
    }

    public <T> List<TagKey<T>> getTags(ResourceKey<Registry<T>> registry, Material material) {
        var holder = getHolder(registry);
        if (holder == null) {
            return List.of();
        }
        return holder.getTags(material);
    }

    protected record HolderEntry<T>(ResourceKey<Registry<T>> registry, TagHolder<T> holder) {

        static <L> HolderEntry<L> create(ResourceKey<Registry<L>> registry, TagHolder<L> holder) {
            return new HolderEntry<>(registry, holder);
        }

        <L> boolean test(ResourceKey<Registry<L>> registry) {
            return registry.equals(this.registry);
        }
    }
}
