package dev.metallurgists.rutile.api.composition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class Content<T> {
    private final List<T> contents = new ArrayList<>();

    protected Content() {
    }

    protected Content(List<T> contents) {
        this.contents.addAll(contents);
    }

    public void add(T content) {
        this.contents.add(content);
    }

    /**
     * Returns a copy of the contents.
     */
    public List<T> getContents() {
        return List.copyOf(this.contents);
    }

    protected abstract Content<T> create(List<T> contents);

    /**
     * Creates a {@link Codec} for a {@link Content} type
     */
    protected static <T, C extends Content<T>> Codec<C> createCodec(
            Codec<T> entryCodec,
            Function<List<T>, C> factory
    ) {
        return RecordCodecBuilder.create(instance -> instance.group(
                entryCodec
                        .listOf()
                        .fieldOf("contents")
                        .forGetter(Content::getContents)
        ).apply(instance, factory));
    }
}

