package dev.metallurgists.rutile.datagen;

import dev.metallurgists.rutile.api.data.provider.composition.AbstractItemCompositionProvider;
import dev.metallurgists.rutile.registry.RutileElements;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
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
