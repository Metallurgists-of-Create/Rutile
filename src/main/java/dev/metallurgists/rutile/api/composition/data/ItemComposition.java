package dev.metallurgists.rutile.api.composition.data;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.metallurgists.rutile.api.composition.SubComposition;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

import java.util.List;

@SuppressWarnings("deprecation")
public record ItemComposition(Item item, List<SubComposition> compositions) {

    public static final Codec<ItemComposition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(ItemComposition::item),
            Codec.list(SubComposition.CODEC).fieldOf("compositions").forGetter(ItemComposition::compositions)
    ).apply(instance, ItemComposition::new));
}
