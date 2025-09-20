package dev.metallurgists.rutile.api.material.registry.fluid;

import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.flag.FlagKey;
import dev.metallurgists.rutile.api.material.flag.types.IFluidRegistry;
import net.minecraft.resources.ResourceLocation;

public interface IMaterialFluid {
    Material getMaterial();

    IFluidRegistry getFlag();

    static ResourceLocation getSourceTexture(Material material, FlagKey<?> flagKey) {
        return ResourceLocation.fromNamespaceAndPath(material.getNamespace(), "block/fluids/" + flagKey.getId().getPath() + "/" + material.getName() + "/still");
    }
    static ResourceLocation getFlowingTexture(Material material, FlagKey<?> flagKey) {
        return ResourceLocation.fromNamespaceAndPath(material.getNamespace(), "block/fluids/" + flagKey.getId().getPath() + "/" + material.getName() + "/flowing");
    }
}
