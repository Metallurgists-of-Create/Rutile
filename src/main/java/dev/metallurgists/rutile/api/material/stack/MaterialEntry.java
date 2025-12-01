package dev.metallurgists.rutile.api.material.stack;

import com.google.common.base.Preconditions;
import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.RutileApi;
import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.tag.TagPrefix;
import dev.metallurgists.rutile.registry.RutileMaterials;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.WeakHashMap;

@Accessors(chain = true, fluent = true)
public class MaterialEntry {
    @NotNull @Getter
    public final TagPrefix tagPrefix;
    @NotNull @Getter
    public final Material material;

    public MaterialEntry(TagPrefix tagPrefix, Material material) {
        this.tagPrefix = Preconditions.checkNotNull(tagPrefix, "MaterialEntry TagPrefix cannot be null!");
        this.material = Preconditions.checkNotNull(material, "MaterialEntry Material cannot be null!");
    }

    public static final MaterialEntry NULL_ENTRY = new MaterialEntry(TagPrefix.NULL_PREFIX, RutileMaterials.Null);

    private static final Map<String, MaterialEntry> PARSE_CACHE = new WeakHashMap<>();

    public MaterialEntry(TagPrefix tagPrefix) {
        this(tagPrefix, RutileMaterials.Null);
    }

    public boolean isEmpty() {
        return this == NULL_ENTRY || material() == RutileMaterials.Null || tagPrefix().isEmpty();
    }

    public boolean isIgnored() {
        return tagPrefix().isIgnored(material());
    }

    @Override
    public String toString() {
        if (tagPrefix.isEmpty()) {
            return material.getId().toString();
        }
        var tags = tagPrefix.getItemTags(material);
        if (tags.isEmpty()) {
            return tagPrefix.id + "/" + material.getName();
        }
        return tags.getFirst().location().toString();
    }

    public static @Nullable MaterialEntry of(Object o) {
        if (o instanceof MaterialEntry entry) return entry;
        if (o instanceof CharSequence chars) {
            var str = chars.toString().trim();
            var cached = PARSE_CACHE.get(str);
            if (cached != null) return cached;

            var values = str.split("\\|", 2);
            if (values.length > 1) {
                ResourceLocation key = Rutile.id(values[0]);
                var prefix = TagPrefix.get(key);
                if (prefix == null) throw new IllegalArgumentException("Invalid TagPrefix: " + key);
                cached = new MaterialEntry(prefix, RutileApi.getMaterialRegistry().getById(Rutile.id(values[1])));
                PARSE_CACHE.put(str, cached);
                return cached;
            }
        }
        return null;
    }
}
