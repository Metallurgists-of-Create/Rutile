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

        DataGenerator dataGenerator = event.getGenerator();
        PackOutput packOutput = dataGenerator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        DefaultCompositions.Item itemCompositions = new DefaultCompositions.Item(packOutput, lookupProvider);
        DefaultCompositions.Fluid fluidCompositions = new DefaultCompositions.Fluid(packOutput, lookupProvider);

        event.addProvider(itemCompositions);
        event.addProvider(fluidCompositions);
    }
}
