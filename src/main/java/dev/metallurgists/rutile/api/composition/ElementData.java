package dev.metallurgists.rutile.api.composition;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.metallurgists.rutile.api.composition.element.Element;
import dev.metallurgists.rutile.api.composition.element.ElementStack;
import dev.metallurgists.rutile.util.ClientUtil;
import lombok.Getter;
import net.minecraft.network.FriendlyByteBuf;

import java.util.ArrayList;
import java.util.List;

public record ElementData(@Getter ElementStack element, @Getter int amount) {
    public static final Codec<ElementData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ElementStack.CODEC.fieldOf("element").forGetter(ElementData::getElement),
            Codec.INT.optionalFieldOf("amount", 1).forGetter(ElementData::getAmount)
    ).apply(instance, ElementData::new));

    public static ElementData create(Element element) {
        return new ElementData(ElementStack.of(element), 1);
    }

    public static ElementData create(ElementStack element) {
        return new ElementData(element, 1);
    }

    public static ElementData create(ElementStack element, int amount) {
        return new ElementData(element, amount);
    }

    public static ElementData create(Element element, int amount) {
        return new ElementData(ElementStack.of(element), amount);
    }

    public ElementData withAmount(int amount) {
        return new ElementData(element, amount);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("amount", getAmount());
        JsonElement elementKey = ElementStack.CODEC.encodeStart(JsonOps.INSTANCE, getElement()).getOrThrow();
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

    public String getDisplay() {
        StringBuilder display = new StringBuilder(element().getElement().symbol());
        if (amount > 1)
            display.append(amount);
        return ClientUtil.toSmallDownNumbers(display.toString());
    }

    public void writeToPacket(FriendlyByteBuf buf) {
        buf.writeResourceLocation(element.id());
        buf.writeInt(amount);
    }

    public static ElementData fromNetwork(FriendlyByteBuf buf) {
        ElementStack element = ElementStack.of(buf.readResourceLocation());
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
