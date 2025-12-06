package dev.metallurgists.rutile.api.material.registry.asset;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.metallurgists.rutile.api.tag.TagPrefix;
import dev.metallurgists.rutile.util.RutileCodecs;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MaterialAsset {
    @Getter
    public final ResourceLocation parentModel;
    @Getter
    public final ResourceLocation tagPrefix;

    @Getter
    public final Map<String, String> stringValues;
    @Getter
    public final Map<String, Boolean> booleanValues;
    @Getter
    public final Map<String, Number> numberValues;

    public static final Codec<MaterialAsset> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            ResourceLocation.CODEC.fieldOf("parent").forGetter(MaterialAsset::getParentModel),
            ResourceLocation.CODEC.fieldOf("tag_prefix").forGetter(MaterialAsset::getParentModel),
            RutileCodecs.stringMapCodec(Codec.STRING).optionalFieldOf("string_values", Map.of()).forGetter(MaterialAsset::getStringValues),
            RutileCodecs.stringMapCodec(Codec.BOOL).optionalFieldOf("boolean_values", Map.of()).forGetter(MaterialAsset::getBooleanValues),
            RutileCodecs.stringMapCodec(RutileCodecs.NUMBER).optionalFieldOf("number_values", Map.of()).forGetter(MaterialAsset::getNumberValues)
    ).apply(inst, MaterialAsset::new));

    public MaterialAsset(ResourceLocation parentModel, ResourceLocation tagPrefix) {
        this(parentModel, tagPrefix, new HashMap<>(), new HashMap<>(), new HashMap<>());
    }

    public MaterialAsset(ResourceLocation parentModel, ResourceLocation tagPrefix, Map<String, String> stringValues, Map<String, Boolean> booleanValues, Map<String, Number> numberValues) {
        this.parentModel = parentModel;
        this.tagPrefix = tagPrefix;
        this.stringValues = stringValues;
        this.booleanValues = booleanValues;
        this.numberValues = numberValues;
    }

    public JsonObject createJson() {
        JsonObject original;
        try (BufferedReader reader = Minecraft.getInstance().getResourceManager()
                .openAsReader(parentModel)) {
            original = GsonHelper.parse(reader, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JsonObject newJson = original.deepCopy();

        getStringValues().forEach(newJson::addProperty);
        getBooleanValues().forEach(newJson::addProperty);
        getNumberValues().forEach(newJson::addProperty);

        return newJson;
    }
}
