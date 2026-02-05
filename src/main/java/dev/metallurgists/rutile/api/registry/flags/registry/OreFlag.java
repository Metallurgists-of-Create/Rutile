package dev.metallurgists.rutile.api.registry.flags.registry;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.material.flags.FlagKey;
import dev.metallurgists.rutile.api.material.flags.IMaterialFlag;
import dev.metallurgists.rutile.api.material.flags.MaterialFlags;
import dev.metallurgists.rutile.registry.RutileMaterials;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class OreFlag implements IMaterialFlag {

    @Getter
    private final List<Material> oreByProducts = new ArrayList<>();
    @Getter
    @Setter
    private int oreMultiplier;
    @Getter
    @Setter
    private int byProductMultiplier;
    @Getter
    @Setter
    private boolean emissive;
    @Getter
    @Setter
    @NotNull
    private Material directSmeltResult = RutileMaterials.Null;

    public OreFlag(int oreMultiplier, int byProductMultiplier) {
        this.oreMultiplier = oreMultiplier;
        this.byProductMultiplier = byProductMultiplier;
        this.emissive = false;
    }

    public OreFlag(int oreMultiplier, int byProductMultiplier, boolean emissive) {
        this.oreMultiplier = oreMultiplier;
        this.byProductMultiplier = byProductMultiplier;
        this.emissive = emissive;
    }

    public OreFlag() {
        this(1, 1);
    }

    public void setOreByProducts(@NotNull Material @NotNull... materials) {
        setOreByProducts(Arrays.asList(materials));
    }

    public void setOreByProducts(@NotNull Collection<@NotNull Material> materials) {
        this.oreByProducts.clear();
        this.oreByProducts.addAll(materials);
    }

    public void addOreByProducts(@NotNull Material @NotNull... materials) {
        this.oreByProducts.addAll(Arrays.asList(materials));
    }

    @NotNull
    public final Material getOreByProduct(int index) {
        if (this.oreByProducts.isEmpty()) return RutileMaterials.Null;
        return this.oreByProducts.get(Mth.clamp(index, 0, this.oreByProducts.size() - 1));
    }

    @NotNull
    public final Material getOreByProduct(int index, @NotNull Material fallback) {
        Material material = getOreByProduct(index);
        return !material.isNull() ? material : fallback;
    }

    @Override
    public void verifyFlag(MaterialFlags properties) {
        properties.ensureSet(FlagKey.DUST, true);

        if (!directSmeltResult.isNull())
            directSmeltResult.getFlags().ensureSet(FlagKey.DUST, true);
        oreByProducts.forEach(m -> m.getFlags().ensureSet(FlagKey.DUST, true));
    }

    @Override
    public JsonObject debugJson() {
        JsonObject jsonObject = new JsonObject();
        if (!oreByProducts.isEmpty()) {
            JsonArray array = new JsonArray();
            for (Material material : oreByProducts) {
                array.add(material.getId().toString());
            }
            jsonObject.add("by_products", array);
        }
        jsonObject.addProperty("ore_multiplier", oreMultiplier);
        jsonObject.addProperty("by_product_multiplier", byProductMultiplier);
        jsonObject.addProperty("emissive", emissive);
        return jsonObject;
    }
}
