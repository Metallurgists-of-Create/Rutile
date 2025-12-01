package dev.metallurgists.rutile.api.composition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record Composition(List<SubComposition> compositions) {

    public static final Codec<Composition> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            SubComposition.CODEC.listOf().fieldOf("compositions").forGetter(Composition::compositions)
    ).apply(inst, Composition::new));
}
