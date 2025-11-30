package dev.metallurgists.rutile.api.composition.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.metallurgists.rutile.api.composition.SubComposition;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record MaterialComposition(ResourceLocation material, List<SubComposition> compositions) {

    public static final Codec<MaterialComposition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("id").forGetter(MaterialComposition::material),
            Codec.list(SubComposition.CODEC).fieldOf("compositions").forGetter(MaterialComposition::compositions)
    ).apply(instance, MaterialComposition::new));
}
