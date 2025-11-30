package dev.metallurgists.rutile.api.material.component.types;

import dev.metallurgists.rutile.api.material.base.Material;
import net.minecraft.resources.ResourceLocation;

public abstract class MaterialComponent {
    private final Material material;

    protected MaterialComponent(Material material) {
        this.material = material;
    }

    public Material getMaterial() {
        return material;
    }

    public ResourceLocation getMaterialKey() {
        return getMaterial().getId();
    }
}
