package dev.metallurgists.rutile.api.data;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;

public interface ISerializable {

    JsonObject toJson();

    default <T> void serialize(Codec<T> codec, T value, String key, JsonObject json) {
        codec.encodeStart(JsonOps.INSTANCE, value).ifSuccess(j -> json.add(key, j));
    }

    default <T> JsonObject serialize(Codec<T> codec, T value) {
        var encoded = codec.encodeStart(JsonOps.INSTANCE, value);
        return encoded.isSuccess() ? encoded.getOrThrow().getAsJsonObject() : new JsonObject();
    }
}
