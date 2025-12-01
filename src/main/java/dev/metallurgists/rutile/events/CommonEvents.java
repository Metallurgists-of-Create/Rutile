package dev.metallurgists.rutile.events;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.registry.ElementRegistry;
import dev.metallurgists.rutile.api.registry.MaterialRegistry;
import dev.metallurgists.rutile.api.registry.TagPrefixRegistry;
import dev.metallurgists.rutile.api.runtime.RutilePackSource;
import dev.metallurgists.rutile.api.runtime.assets.RutileDynamicResourcePack;
import dev.metallurgists.rutile.api.runtime.data.RutileDynamicDataPack;
import dev.metallurgists.rutile.debug.material.DebugElementPrinter;
import dev.metallurgists.rutile.debug.material.DebugMaterialPrinter;
import dev.metallurgists.rutile.debug.material.DebugTagPrefixPrinter;
import dev.metallurgists.rutile.registry.RutileMaterialBlocks;
import dev.metallurgists.rutile.registry.RutileMaterialItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

public class CommonEvents {

    private static IEventBus modBus;

    public static void init(final IEventBus modBus) {
        CommonEvents.modBus = modBus;
        modBus.register(CommonEvents.class);
    }

    // Only register everything once.
    private static boolean didRunRegistration = false;

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onRegisterEarly(RegisterEvent event) {
        if (!didRunRegistration) {
            TagPrefixRegistry.getInstance().onRegisterObjects(event);
            MaterialRegistry.getInstance().setAllowRegistration(true);
            MaterialRegistry.getInstance().registerAll();
            ElementRegistry.getInstance().onRegisterObjects(event);
            Rutile.LOGGER.info("Registered rutile registries");
            didRunRegistration = true;
        }
        registerMaterials(event);
    }

    private static void registerMaterials(RegisterEvent event) {
        event.register(Registries.BLOCK, registry -> {
            RutileMaterialBlocks.generateMaterialBlocks();
        });
        event.register(Registries.ITEM, registry -> {
            RutileMaterialItems.generateMaterialItems();
        });
    }


    @SubscribeEvent
    public static void loadComplete(FMLLoadCompleteEvent event) {
        if (!FMLEnvironment.production) {
            DebugTagPrefixPrinter.print();
            DebugMaterialPrinter.print();
            DebugElementPrinter.print();
        }
        MaterialRegistry.getInstance().onLoadComplete();
        ElementRegistry.getInstance().onLoadComplete();
        TagPrefixRegistry.getInstance().onLoadComplete();
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
