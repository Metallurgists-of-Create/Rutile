package dev.metallurgists.rutile.datagen;

import dev.metallurgists.rutile.api.data.server.provider.composition.AbstractItemCompositionProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class RutileItemCompositions extends AbstractItemCompositionProvider {
    public RutileItemCompositions(String modId, PackOutput output, ExistingFileHelper fileHelper, CompletableFuture<HolderLookup.Provider> registries) {
        super(modId, output, fileHelper, registries);
    }

    @Override
    public void generate(HolderLookup.Provider registries, ExistingFileHelper fileHelper) {

    }
}
