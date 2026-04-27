package dev.metallurgists.rutile.api.material.stack;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.RutileClient;
import dev.metallurgists.rutile.api.data.ISerializable;
import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.material.module.UnitSizeModule;
import dev.metallurgists.rutile.api.registry.RutileRegistries;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.WeakHashMap;

public class MaterialStack implements ISerializable {
    public static final MaterialStack EMPTY = MaterialStack.of((ResourceLocation) null, -1);
    public static final Codec<MaterialStack> CODEC;
    public static final Codec<MaterialStack> SINGLE_CODEC;
    @Getter
    @Setter
    private long amount;
    @Getter
    private final ResourceLocation id;

    public MaterialStack(ResourceLocation id) {
        this.id = id;
    }

    public MaterialStack(ResourceLocation id, long amount) {
        this.id = id;
        this.amount = amount;
    }

    public static MaterialStack of(ResourceLocation id) {
        return new MaterialStack(id, UnitSizeModule.UNIT);
    }

    public static MaterialStack of(ResourceLocation id, long amount) {
        return new MaterialStack(id, amount);
    }

    public static MaterialStack of(Material material) {
        return new MaterialStack(material.getId(), UnitSizeModule.UNIT);
    }

    public static MaterialStack of(Material material, long amount) {
        return new MaterialStack(material.getId(), amount);
    }

    private static final Map<String, MaterialStack> PARSE_CACHE = new WeakHashMap<>();

    public MaterialStack copy() {
        if (isEmpty()) return EMPTY;
        return new MaterialStack(id, amount);
    }

    public MaterialStack add(long amount) {
        return new MaterialStack(id, this.amount + amount);
    }

    public MaterialStack multiply(long amount) {
        return new MaterialStack(id, this.amount * amount);
    }

    public MaterialStack divide(long amount) {
        return new MaterialStack(id, this.amount / amount);
    }

    public Material getMaterial() {
        return RutileRegistries.MATERIALS.get(id);
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

        cached = new MaterialStack(Rutile.id(copy), count);
        PARSE_CACHE.put(trimmed, cached);
        return cached;
    }

    public boolean isEmpty() {
        return this.id == null || this.amount < 1;
    }

    @Override
    public String toString() {
        String string = "";
        if (this.isEmpty()) return "";
        if (getMaterial().getComposition() == null || getMaterial().getComposition().compositions().isEmpty()) {
            string += "?";
        }  else {
            string += getMaterial().getComposition().getString();
        }
        if (amount > 1) {
            string += RutileClient.toSmallDownNumbers(Long.toString(amount));
        }
        return string;
    }

    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ResourceLocation.CODEC.fieldOf("id").forGetter(MaterialStack::getId),
                Codec.LONG.fieldOf("amount").forGetter(MaterialStack::getAmount)
        ).apply(instance, MaterialStack::new));
        SINGLE_CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ResourceLocation.CODEC.fieldOf("id").forGetter(MaterialStack::getId)
        ).apply(instance, MaterialStack::new));
    }

    @Override
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        serialize(ResourceLocation.CODEC, getId(), "id", json);
        serialize(Codec.LONG, getAmount(), "amount", json);
        return json;
    }
}
