package dev.metallurgists.rutile.api.material.modifier;

import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.registry.RutileAPI;
import dev.metallurgists.rutile.api.registry.material.MaterialRegistry;
import dev.metallurgists.rutile.registry.RutileRegistries;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MaterialGroup {
    private final ResourceLocation key;
    @Getter
    private List<Material> materials;

    private MaterialGroup(ResourceLocation key) {
        this.key = key;
        this.materials = new ArrayList<>();
    }

    public static MaterialGroup create(ResourceLocation key) {
        return new MaterialGroup(key);
    }

    public MaterialGroup withMaterials(Material... materials) {
        this.materials.addAll(Arrays.asList(materials));
        return this;
    }

    public void withKeyWord(String keyWord) {
        List<Material> materials = new ArrayList<>();
        for (Material material : RutileRegistries.MATERIAL_REGISTRY) {
            if (material.getName().contains(keyWord)) {
                materials.add(material);
            }
        }
        this.materials.addAll(materials);
    }
}
