package dev.metallurgists.rutile.api.data.provider.composition;

import dev.metallurgists.rutile.api.material.Material;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public abstract class AbstractMaterialCompositionProvider extends AbstractCompositionProvider<Material> {

    public AbstractMaterialCompositionProvider(String modId, PackOutput output, ExistingFileHelper fileHelper, CompletableFuture<HolderLookup.Provider> registries) {
        super(modId, "material", output, fileHelper, registries);
    }

    @Override
    ResourceLocation getKey(Material value) {
        return value.getId();
    }
}
