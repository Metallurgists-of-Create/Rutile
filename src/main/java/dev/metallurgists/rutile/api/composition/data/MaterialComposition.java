package dev.metallurgists.rutile.api.composition.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.metallurgists.rutile.api.composition.SubComposition;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.registry.RutileAPI;

import java.util.List;

public record MaterialComposition(Material material, List<SubComposition> compositions) {

    public static final Codec<MaterialComposition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            RutileAPI.materialManager.codec().fieldOf("material").forGetter(MaterialComposition::material),
            Codec.list(SubComposition.CODEC).fieldOf("compositions").forGetter(MaterialComposition::compositions)
    ).apply(instance, MaterialComposition::new));
}
