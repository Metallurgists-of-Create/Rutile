package dev.metallurgists.rutile.datagen;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.data.server.provider.composition.AbstractFluidCompositionProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class RutileFluidCompositions extends AbstractFluidCompositionProvider {

    public RutileFluidCompositions(PackOutput output, ExistingFileHelper fileHelper, CompletableFuture<HolderLookup.Provider> registries) {
        super(Rutile.ID, output, fileHelper, registries);
    }

    @Override
    public void generate(HolderLookup.Provider registries, ExistingFileHelper fileHelper) {
        //addData(Fluids.WATER, Composition.builder().compositions(List.of(SubComposition.builder().element(H.asStack(2), O.asStack()).build())).build());
    }
}
