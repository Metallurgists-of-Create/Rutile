package dev.metallurgists.rutile;

import dev.metallurgists.rutile.api.dynamic_pack.RutilePackSource;
import dev.metallurgists.rutile.api.dynamic_pack.asset.RutileDynamicResourcePack;
import dev.metallurgists.rutile.api.dynamic_pack.data.RuntimeCompositions;
import dev.metallurgists.rutile.api.dynamic_pack.data.RutileDynamicDataPack;
import dev.metallurgists.rutile.api.dynamic_pack.data.recipe.RutileRecipes;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class CommonEvents {

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModBusEvents {

        @SubscribeEvent
        public static void addPackFinders(AddPackFindersEvent event) {
            if (event.getPackType() == PackType.CLIENT_RESOURCES) {
                // Clear old data
                RutileDynamicResourcePack.clearClient();

                event.addRepositorySource(new RutilePackSource("rutile:dynamic_assets",
                        event.getPackType(),
                        Pack.Position.BOTTOM,
                        RutileDynamicResourcePack::new));
            } else if (event.getPackType() == PackType.SERVER_DATA) {
                RutileDynamicDataPack.clearServer();

                long startTime = System.currentTimeMillis();
                RutileRecipes.recipeRemoval();
                RutileRecipes.recipeAddition(RutileDynamicDataPack::addRecipe);
                RuntimeCompositions.compositionAddition(RutileDynamicDataPack::addComposition);
                Rutile.LOGGER.info("Rutile Data loading took {}ms", System.currentTimeMillis() - startTime);
                event.addRepositorySource(new RutilePackSource("rutile:dynamic_data", event.getPackType(), Pack.Position.BOTTOM, RutileDynamicDataPack::new));
            }
        }
    }
}
