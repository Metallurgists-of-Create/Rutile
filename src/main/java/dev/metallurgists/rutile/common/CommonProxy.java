package dev.metallurgists.rutile.common;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.dynamic_pack.RutilePackSource;
import dev.metallurgists.rutile.api.dynamic_pack.asset.RutileDynamicResourcePack;
import dev.metallurgists.rutile.api.dynamic_pack.data.RuntimeCompositions;
import dev.metallurgists.rutile.api.dynamic_pack.data.RutileDynamicDataPack;
import dev.metallurgists.rutile.api.dynamic_pack.data.recipe.RutileRecipes;
import dev.metallurgists.rutile.api.registry.RutileAPI;
import dev.metallurgists.rutile.api.registry.RutileRegistries;
import dev.metallurgists.rutile.api.registry.material.MaterialRegistryManager;
import dev.metallurgists.rutile.config.RutileConfig;
import dev.metallurgists.rutile.registry.*;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddPackFindersEvent;


public class CommonProxy {

    public CommonProxy() {
        IEventBus modEventBus = ModLoadingContext.get().getActiveContainer().getEventBus();
        ModLoadingContext modLoadingContext = ModLoadingContext.get();
        modEventBus.register(this);

        NeoForge.EVENT_BUS.register(new CommonEventHandler());

        RutileConfig.register(modLoadingContext);

        RutileRegistries.init();
    }

    public static void init() {
        RutileElements.staticInit();
        initMaterials();
        RutileFlagKeys.init();

        RutileFluids.register();
        RutileBlocks.register();
        RutileItems.register();

        IEventBus modEventBus = ModLoadingContext.get().getActiveContainer().getEventBus();

        Rutile.registrate.registerEventListeners(modEventBus);
    }

    public static void initMaterials() {
        RutileMaterials.init();
    }

    @SubscribeEvent
    public void modConstruct(FMLConstructModEvent event) {
        event.enqueueWork(CommonProxy::init);
    }

    @SubscribeEvent
    public void addPackFinders(AddPackFindersEvent event) {
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
