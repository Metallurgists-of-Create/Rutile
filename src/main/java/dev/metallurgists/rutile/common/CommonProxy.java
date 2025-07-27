package dev.metallurgists.rutile.common;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.dynamic_pack.RutilePackSource;
import dev.metallurgists.rutile.api.dynamic_pack.asset.RutileDynamicResourcePack;
import dev.metallurgists.rutile.api.dynamic_pack.data.RuntimeCompositions;
import dev.metallurgists.rutile.api.dynamic_pack.data.RutileDynamicDataPack;
import dev.metallurgists.rutile.api.dynamic_pack.data.recipe.RutileRecipes;
import dev.metallurgists.rutile.api.material.events.MaterialEvent;
import dev.metallurgists.rutile.api.material.events.MaterialRegistryEvent;
import dev.metallurgists.rutile.api.material.events.PostMaterialEvent;
import dev.metallurgists.rutile.api.registry.RutileAPI;
import dev.metallurgists.rutile.api.registry.RutileRegistries;
import dev.metallurgists.rutile.api.registry.material.MaterialRegistryManager;
import dev.metallurgists.rutile.config.RutileConfig;
import dev.metallurgists.rutile.registry.*;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class CommonProxy {

    public CommonProxy() {
        ModLoadingContext modLoadingContext = ModLoadingContext.get();
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.register(this);

        MinecraftForge.EVENT_BUS.register(new CommonEventHandler());

        RutileAPI.materialManager = MaterialRegistryManager.getInstance();

        RutileConfig.register(modLoadingContext);

        RutileRegistries.init();
    }

    public static void init() {
        RutileElements.init();
        initMaterials();
        RutileFlagKeys.init();

        RutileFluids.register();
        RutileBlocks.register();
        RutileItems.register();

        Rutile.registrate.registerRegistrate();
    }

    public static void initMaterials() {
        MaterialRegistryManager managerInternal = (MaterialRegistryManager) RutileAPI.materialManager;

        Rutile.LOGGER.info("Registering material registries");
        ModLoader.get().postEvent(new MaterialRegistryEvent());

        managerInternal.unfreezeRegistries();
        Rutile.LOGGER.info("Registering Rutile Materials");

        RutileMaterials.init();
        MaterialRegistryManager.getInstance()
                .getRegistry(Rutile.ID)
                .setFallbackMaterial(RutileMaterials.Null);

        Rutile.LOGGER.info("Registering plugin Materials");
        MaterialEvent materialEvent = new MaterialEvent();
        ModLoader.get().postEvent(materialEvent);

        managerInternal.closeRegistries();
        ModLoader.get().postEvent(new PostMaterialEvent());

        managerInternal.freezeRegistries();
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
