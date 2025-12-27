package dev.metallurgists.rutile.api.data.server.provider.composition;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public abstract class AbstractFluidCompositionProvider extends AbstractCompositionProvider<Fluid> {

    public AbstractFluidCompositionProvider(String modId, PackOutput output, ExistingFileHelper fileHelper, CompletableFuture<HolderLookup.Provider> registries) {
        super(modId, "fluid", output, fileHelper, registries);
    }

    @Override
    ResourceLocation getKey(Fluid value) {
        return BuiltInRegistries.FLUID.getKey(value);
    }
}
