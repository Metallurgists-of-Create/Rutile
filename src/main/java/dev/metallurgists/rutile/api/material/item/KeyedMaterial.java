package dev.metallurgists.rutile.api.material.item;

import com.google.common.base.Preconditions;
import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.flag.FlagKey;
import dev.metallurgists.rutile.api.material.flag.types.IHaveTags;
import dev.metallurgists.rutile.api.registrate.builder.MaterialEntry;
import dev.metallurgists.rutile.api.registry.RutileAPI;
import dev.metallurgists.rutile.registry.RutileMaterials;
import dev.metallurgists.rutile.registry.RutileRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public record KeyedMaterial(@NotNull FlagKey<?> flagKey, @NotNull Material material) {

    public KeyedMaterial {
        Preconditions.checkNotNull(flagKey, "KeyedMaterial FlagKey cannot be null!");
        Preconditions.checkNotNull(material, "KeyedMaterial Material cannot be null!");
    }

    public static final KeyedMaterial NULL_ENTRY = new KeyedMaterial(FlagKey.EMPTY, RutileMaterials.Null);

    private static final Map<String, KeyedMaterial> PARSE_CACHE = new WeakHashMap<>();

    public KeyedMaterial(FlagKey<?> flagKey) {
        this(flagKey, RutileMaterials.Null);
    }

    public boolean isEmpty() {
        return this == NULL_ENTRY || material() == RutileMaterials.Null || flagKey().isEmpty();
    }

    public boolean isIgnored() {
        return material().noRegister(flagKey());
    }

    @Override
    public String toString() {
        if (flagKey.isEmpty()) {
            return material.getId().toString();
        }
        List<TagKey<Item>> tags = new ArrayList<>();
        if (flagKey instanceof IHaveTags t && t.getTagHolder().has(Registries.ITEM)) tags.addAll(t.getTagHolder().getTags(Registries.ITEM, material));
        if (tags.isEmpty()) {
            return flagKey.getId().getPath() + "/" + material.getName();
        }
        return tags.get(0).location().toString();
    }

    public static @Nullable KeyedMaterial of(Object o) {
        if (o instanceof KeyedMaterial entry) return entry;
        if (o instanceof CharSequence chars) {
            var str = chars.toString().trim();
            var cached = PARSE_CACHE.get(str);
            if (cached != null) return cached;

            var values = str.split("\\|", 2);
            String flag = values[0];
            String mat = values[1];
            ResourceLocation flagLoc = Rutile.id(flag);
            ResourceLocation matLoc = Rutile.id(mat);
            if (values.length > 2) {
                var flagK = RutileAPI.getRegisteredFlags().get(flagLoc);
                if (flagK == null) throw new IllegalArgumentException("Invalid FlagKey: " + values[0]);
                var material = RutileAPI.materialRegistry.get(matLoc);
                if (material == null) throw new IllegalArgumentException("Invalid Material: " + matLoc);
                cached = new KeyedMaterial(flagK, material);
                PARSE_CACHE.put(str, cached);
                return cached;
            }
        }
        return null;
    }
}
