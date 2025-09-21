package dev.metallurgists.rutile.api.composition;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.metallurgists.rutile.api.composition.element.Element;
import dev.metallurgists.rutile.api.registry.CustomRutileRegistries;
import dev.metallurgists.rutile.registry.RutileRegistries;
import dev.metallurgists.rutile.util.ClientUtil;
import lombok.Getter;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public record ElementData(@Getter ResourceKey<Element> element, @Getter int amount) {
    public static final Codec<ElementData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceKey.codec(CustomRutileRegistries.ELEMENT_REGISTRY).fieldOf("element").forGetter(ElementData::getElement),
            Codec.INT.optionalFieldOf("amount", 1).forGetter(ElementData::getAmount)
    ).apply(instance, ElementData::new));

    public static ElementData create(ResourceKey<Element> element) {
        return new ElementData(element, 1);
    }

    public static ElementData create(ResourceKey<Element> element, int amount) {
        return new ElementData(element, amount);
    }

    public ElementData withAmount(int amount) {
        return new ElementData(element, amount);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("amount", getAmount());
        JsonElement elementKey = ResourceKey.codec(CustomRutileRegistries.ELEMENT_REGISTRY).encodeStart(JsonOps.INSTANCE, getElement()).getOrThrow();
        json.add("element", elementKey);
        return json;
    }

    public static List<SubComposition> createComposition(SubComposition.Builder... subCompositionBuilders) {
        List<SubComposition> subCompositions = new ArrayList<>();
        for (SubComposition.Builder builder : subCompositionBuilders) {
            subCompositions.add(builder.build());
        }
        return subCompositions;
    }

    public static List<SubComposition> createFromList(List<ElementData> elementDataList) {
        List<SubComposition> subCompositions = new ArrayList<>();
        for (ElementData elementData : elementDataList) {
            subCompositions.add(new SubComposition(List.of(elementData), 1));
        }
        return subCompositions;
    }

    public String getDisplay(HolderLookup.Provider registries) {
        var lookup = registries.lookupOrThrow(CustomRutileRegistries.ELEMENT_REGISTRY);
        StringBuilder display = new StringBuilder(lookup.get(element).orElseThrow().value().symbol());
        if (amount > 1)
            display.append(amount);
        return ClientUtil.toSmallDownNumbers(display.toString());
    }

    public void writeToPacket(FriendlyByteBuf buf) {
        buf.writeResourceLocation(element.location());
        buf.writeInt(amount);
    }

    public static ElementData fromNetwork(FriendlyByteBuf buf) {
        ResourceKey<Element> element = ResourceKey.create(CustomRutileRegistries.ELEMENT_REGISTRY, buf.readResourceLocation());
        int amount = buf.readInt();
        return new ElementData(element, amount);
    }

    public static List<ElementData> listFromNetwork(FriendlyByteBuf buf) {
        int size = buf.readVarInt();
        List<ElementData> elements = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            elements.add(fromNetwork(buf));
        }
        return elements;
    }
}
