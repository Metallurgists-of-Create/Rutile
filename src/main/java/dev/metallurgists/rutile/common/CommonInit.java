package dev.metallurgists.rutile.common;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.RutileDataGen;
import dev.metallurgists.rutile.api.composition.data.ItemComposition;
import dev.metallurgists.rutile.api.composition.data.MaterialComposition;
import dev.metallurgists.rutile.api.dynamic_pack.RutilePackSource;
import dev.metallurgists.rutile.api.dynamic_pack.asset.RutileDynamicResourcePack;
import dev.metallurgists.rutile.api.dynamic_pack.data.RutileDynamicDataPack;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.builder.FlagContainer;
import dev.metallurgists.rutile.api.material.debug.DebugMaterialPrinter;
import dev.metallurgists.rutile.api.material.registry.fluid.MaterialBucketItem;
import dev.metallurgists.rutile.api.registry.CustomRutileRegistries;
import dev.metallurgists.rutile.api.registry.RutileAPI;
import dev.metallurgists.rutile.config.RutileConfig;
import dev.metallurgists.rutile.registry.*;
import net.minecraft.client.model.geom.builders.MaterialDefinition;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.fluids.capability.wrappers.FluidBucketWrapper;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.RegisterEvent;


public class CommonInit {

    private static IEventBus modBus;

    public static void init(final IEventBus modBus) {
        CommonInit.modBus = modBus;
        ModLoadingContext modLoadingContext = ModLoadingContext.get();

        RutileConfig.register(modLoadingContext);
        modBus.addListener(RutileDataGen::gatherDataEvent);
        modBus.register(CommonInit.class);
        Rutile.registrate.registerEventListeners(modBus);
    }

    // Only register everything once.
    private static boolean didRunRegistration = false;

    @SubscribeEvent
    public static void onRegister(RegisterEvent event) {
        if (didRunRegistration) {
            return;
        }
        didRunRegistration = true;
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onRegisterEarly(RegisterEvent event) {
        MaterialRegistry.getInstance().setAllowRegistration(true);
        MaterialRegistry.getInstance().onRegisterObjects(event);
        MaterialRegistry.getInstance().setAllowRegistration(false);
        ElementRegistry.getInstance().setAllowRegistration(true);
        ElementRegistry.getInstance().onRegisterObjects(event);
        ElementRegistry.getInstance().setAllowRegistration(false);
    }

    @SubscribeEvent
    public static void newDatapackRegistry(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(CustomRutileRegistries.ITEM_COMPOSITION_REGISTRY, ItemComposition.CODEC, ItemComposition.CODEC);
        event.dataPackRegistry(CustomRutileRegistries.MATERIAL_COMPOSITION_REGISTRY, MaterialComposition.CODEC, MaterialComposition.CODEC);
    }

    @SubscribeEvent
    public static void loadComplete(FMLLoadCompleteEvent event) {
        DebugMaterialPrinter.print();
        for (Material material : RutileAPI.getMaterialRegistry().getAll()) {
            Rutile.LOGGER.debug("[DEBUG] Loading material {}", material.getId());
            for (FlagContainer<?> flagContainer : material.getFlags().getFlagContainers()) {
                Rutile.LOGGER.debug("{} Objects:", flagContainer.getRegistryType().resourceKey().location());
                for (var key : flagContainer.getObjects().values()) {
                    Rutile.LOGGER.debug("> {}", key);
                }
                Rutile.LOGGER.debug("{} Builders:", flagContainer.getRegistryType().resourceKey().location());
                for (var builder : flagContainer.getBuilders().values()) {
                    Rutile.LOGGER.debug("> {}", builder.getBuilderName());
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        for (Item item : BuiltInRegistries.ITEM) {
            if (item instanceof MaterialBucketItem) {
                event.registerItem(Capabilities.FluidHandler.ITEM,
                        (stack, ctx) -> new FluidBucketWrapper(stack), item);
            }
        }
    }

    @SubscribeEvent
    public static void addPackFinders(AddPackFindersEvent event) {
        if (event.getPackType() == PackType.CLIENT_RESOURCES) {
            RutileDynamicResourcePack.clearClient();

            event.addRepositorySource(new RutilePackSource("rutile:dynamic_assets",
                    event.getPackType(),
                    Pack.Position.BOTTOM,
                    RutileDynamicResourcePack::new));
        } else if (event.getPackType() == PackType.SERVER_DATA) {
            RutileDynamicDataPack.clearServer();

            event.addRepositorySource(new RutilePackSource("rutile:dynamic_data",
                    event.getPackType(),
                    Pack.Position.BOTTOM,
                    RutileDynamicDataPack::new));
        }
    }
}
