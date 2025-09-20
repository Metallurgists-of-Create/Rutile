package dev.metallurgists.rutile.api.material.flag;

import dev.metallurgists.rutile.api.material.base.MaterialFlags;
import net.minecraft.resources.ResourceLocation;

public interface IMaterialFlag {
    FlagKey<?> getKey();

    void verifyFlag(MaterialFlags flags);

    default ResourceLocation loc(String namespace, String path) {
        return ResourceLocation.fromNamespaceAndPath(namespace, path);
    }
}
