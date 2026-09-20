package dev.metallurgists.rutile.api.composition.content;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

/**
 * Holds the registry entries a composition applies to. Each entry is either a direct registry entry, or a {@link TagKey}.
 */
public class HolderContent<T> {
    private final List<Either<T, TagKey<T>>> contents = new ArrayList<>();

    public HolderContent() {
    }

    public HolderContent(List<Either<T, TagKey<T>>> contents) {
        this.contents.addAll(contents);
    }

    public static <T> HolderContent<T> of(List<? extends T> entries) {
        HolderContent<T> holderContent = new HolderContent<>();
        for (T entry : entries) {
            holderContent.add(entry);
        }
        return holderContent;
    }

    public static <T> HolderContent<T> of(TagKey<T> tag) {
        return new HolderContent<T>().add(tag);
    }

    public static <T> HolderContent<T> of(List<? extends T> entries, List<TagKey<T>> tags) {
        HolderContent<T> holderContent = of(entries);
        for (TagKey<T> tag : tags) {
            holderContent.add(tag);
        }
        return holderContent;
    }

    public HolderContent<T> add(T entry) {
        this.contents.add(Either.left(entry));
        return this;
    }

    public HolderContent<T> add(TagKey<T> tag) {
        this.contents.add(Either.right(tag));
        return this;
    }

    public List<Either<T, TagKey<T>>> getContents() {
        return List.copyOf(this.contents);
    }

    /**
     * Resolves the held entries against the given registry, expanding any {@link TagKey}s.
     */
    public List<T> resolve(Registry<T> registry) {
        List<T> resolved = new ArrayList<>();
        for (Either<T, TagKey<T>> content : this.contents) {
            content.ifLeft(resolved::add);
            content.ifRight(tag -> StreamSupport.stream(registry.getTagOrEmpty(tag).spliterator(), false).map(Holder::value).forEach(resolved::add));
        }
        return resolved;
    }

    /**
     * Creates a {@link Codec} for a {@link HolderContent}. Encodes as a JSON array whose elements
     * are either a registry entry id or a {@code #}-prefixed tag id.
     */
    public static <T> Codec<HolderContent<T>> codec(ResourceKey<? extends Registry<T>> registryKey, Codec<T> entryCodec) {
        Codec<Either<T, TagKey<T>>> elementCodec = Codec.either(entryCodec, TagKey.hashedCodec(registryKey));
        return elementCodec.listOf().xmap(HolderContent::new, HolderContent::getContents);
    }
}