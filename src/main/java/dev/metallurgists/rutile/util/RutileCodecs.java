package dev.metallurgists.rutile.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.PrimitiveCodec;

import java.util.Map;

public class RutileCodecs {

    public static PrimitiveCodec<Number> NUMBER = new PrimitiveCodec<Number>() {
        @Override
        public <T> DataResult<Number> read(final DynamicOps<T> ops, final T input) {
            return ops.getNumberValue(input);
        }

        @Override
        public <T> T write(final DynamicOps<T> ops, final Number value) {
            return ops.createNumeric(value);
        }

        @Override
        public String toString() {
            return "Number";
        }
    };



    public static <T> Codec<Map<String, T>> stringMapCodec(Codec<T> valueCodec) {
        return Codec.unboundedMap(Codec.STRING, valueCodec);
    }
}
