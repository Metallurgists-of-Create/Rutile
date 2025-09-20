package dev.metallurgists.rutile.api.material.base;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.registry.RutileMaterials;
import dev.metallurgists.rutile.registry.RutileRegistries;
import dev.metallurgists.rutile.util.ClientUtil;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.WeakHashMap;

public record MaterialStack(@NotNull Material material, long amount) {

    public static final MaterialStack EMPTY = new MaterialStack(RutileMaterials.Null.get(), 0);

    private static final Map<String, MaterialStack> PARSE_CACHE = new WeakHashMap<>();

    public MaterialStack copy() {
        if (isEmpty()) return EMPTY;
        return new MaterialStack(material, amount);
    }

    public MaterialStack add(long amount) {
        return new MaterialStack(material, this.amount + amount);
    }

    public MaterialStack multiply(long amount) {
        return new MaterialStack(material, this.amount * amount);
    }

    public MaterialStack divide(long amount) {
        return new MaterialStack(material, this.amount / amount);
    }

    public static MaterialStack fromString(CharSequence str) {
        String trimmed = str.toString().trim();
        String copy = trimmed;

        var cached = PARSE_CACHE.get(trimmed);

        if (cached != null) {
            return cached;
        }

        var count = 1;
        var spaceIndex = copy.indexOf(' ');

        if (spaceIndex >= 2 && copy.indexOf('x') == spaceIndex - 1) {
            count = Integer.parseInt(copy.substring(0, spaceIndex - 1));
            copy = copy.substring(spaceIndex + 1);
        }

        ResourceLocation key = Rutile.id(copy);
        Material mat = RutileRegistries.MATERIAL_REGISTRY.getOptional(key).orElse(RutileMaterials.Null.get());
        cached = new MaterialStack(mat, count);
        PARSE_CACHE.put(trimmed, cached);
        return cached;
    }

    public boolean isEmpty() {
        return this.material == RutileMaterials.Null.get() || this.amount < 1;
    }

    @Override
    public String toString() {
        String string = "";
        if (this.isEmpty()) return "";
        if (material.getComposition() == null || material.getComposition().isEmpty()) {
            string += "?";
        } else {
            string += material.getComposition();
        }
        if (amount > 1) {
            string += ClientUtil.toSmallDownNumbers(Long.toString(amount));
        }
        return string;
    }
}
