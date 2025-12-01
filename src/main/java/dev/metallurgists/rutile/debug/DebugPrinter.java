package dev.metallurgists.rutile.debug;

import com.google.common.hash.Hashing;
import com.google.common.hash.HashingOutputStream;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonWriter;
import dev.metallurgists.rutile.Rutile;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.ToIntFunction;

@SuppressWarnings("UnstableApiUsage")
public interface DebugPrinter {
    AtomicInteger INDENT_WIDTH = new AtomicInteger(2);
    ToIntFunction<String> FIXED_ORDER_FIELDS = Util.make(new Object2IntOpenHashMap<>(), (map) -> {
        map.put("neoforge:conditions", -1);
        map.put("type", 0);
        map.put("parent", 1);
        map.defaultReturnValue(2);
    });
    Comparator<String> KEY_COMPARATOR = Comparator.comparingInt(FIXED_ORDER_FIELDS).thenComparing((p_236077_) -> p_236077_);

    @ApiStatus.Internal
    default void writeJson(ResourceLocation id, @Nullable String subDir, Path parent, JsonElement json) {
        try {
            Path file;
            if (subDir != null) {
                file = parent.resolve(id.getNamespace()).resolve(subDir).resolve(id.getPath() + ".json");
            } else {
                file = parent.resolve(id.getNamespace()).resolve(id.getPath());
            }
            ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
            HashingOutputStream hashingoutputstream = new HashingOutputStream(Hashing.sha256(), bytearrayoutputstream);

            try (JsonWriter jsonwriter = new JsonWriter(new OutputStreamWriter(hashingoutputstream, StandardCharsets.UTF_8))) {
                jsonwriter.setSerializeNulls(false);
                jsonwriter.setIndent(" ".repeat(Math.max(0, INDENT_WIDTH.get())));
                GsonHelper.writeValue(jsonwriter, json, KEY_COMPARATOR);
            }
            Files.createDirectories(file.getParent());
            Files.write(file, bytearrayoutputstream.toByteArray());

        } catch (IOException e) {
            Rutile.LOGGER.error("Failed to write JSON export for file {}", id, e);
        }
    }

}
