package dev.metallurgists.rutile;

import dev.metallurgists.rutile.datagen.RutileFluidCompositions;
import dev.metallurgists.rutile.datagen.RutileItemCompositions;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

public class RutileDataGen {

    public static void gatherDataEvent(GatherDataEvent event) {
        Rutile.LOGGER.info("[Rutile] Data Generation starts.");
        String modId = Rutile.ID;
        DataGenerator dataGenerator = event.getGenerator();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();
        PackOutput packOutput = dataGenerator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        final boolean includeClient = event.includeClient();
        final boolean includeServer = event.includeServer();

        RutileItemCompositions itemCompositions = new RutileItemCompositions(modId, packOutput, fileHelper, lookupProvider);
        RutileFluidCompositions fluidCompositions = new RutileFluidCompositions(packOutput, fileHelper, lookupProvider);

        dataGenerator.addProvider(includeServer, itemCompositions);
        dataGenerator.addProvider(includeServer, fluidCompositions);
    }
}
