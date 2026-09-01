package dev.metallurgists.rutile.datagen;

import com.tterrag.registrate.providers.ProviderType;
import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.data.provider.composition.FluidCompositionProvider;
import dev.metallurgists.rutile.api.data.provider.composition.ItemCompositionProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

public class RutileDataGen {
    public static final ProviderType<ItemCompositionProvider.Registrate> ITEM_COMPOSITION = ProviderType.registerServerData("item_composition", ItemCompositionProvider.Registrate::new);
    public static final ProviderType<FluidCompositionProvider.Registrate> FLUID_COMPOSITION = ProviderType.registerServerData("fluid_composition", FluidCompositionProvider.Registrate::new);

    public static void gatherDataEvent(GatherDataEvent event) {
        Rutile.LOGGER.info("[Rutile] Data Generation starts.");

        String modId = Rutile.ID;
        DataGenerator dataGenerator = event.getGenerator();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();
        PackOutput packOutput = dataGenerator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        final boolean includeClient = event.includeClient();
        final boolean includeServer = event.includeServer();

        DebugCompositions.Item itemCompositions = new DebugCompositions.Item(packOutput, lookupProvider);
        DebugCompositions.Fluid fluidCompositions = new DebugCompositions.Fluid(packOutput, lookupProvider);

        dataGenerator.addProvider(includeServer, itemCompositions);
        dataGenerator.addProvider(includeServer, fluidCompositions);
    }
}
