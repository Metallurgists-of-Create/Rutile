package dev.metallurgists.rutile.api.material.registry.fluid;

import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.builder.FlagSource;
import dev.metallurgists.rutile.api.material.builder.MaterialFluidBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;

public interface IMaterialFluid {
    Material getMaterial();

    MaterialFluidBuilder getBuilder();

    static ResourceLocation getSourceTexture(Material material, FlagSource<Fluid> flagSource) {
        return ResourceLocation.fromNamespaceAndPath(material.getNamespace(), "block/fluids/" + flagSource.modid() + "_" + flagSource.name() + "/" + material.getName() + "/still");
    }
    static ResourceLocation getFlowingTexture(Material material, FlagSource<Fluid> flagSource) {
        return ResourceLocation.fromNamespaceAndPath(material.getNamespace(), "block/fluids/" + flagSource.modid() + "_" + flagSource.name() + "/" + material.getName() + "/flowing");
    }
}
