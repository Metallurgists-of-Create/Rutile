package dev.metallurgists.rutile.common;

import com.google.common.collect.Multimaps;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.providers.RegistrateProvider;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.RutileDataGen;
import dev.metallurgists.rutile.api.composition.data.ItemComposition;
import dev.metallurgists.rutile.api.composition.data.MaterialComposition;
import dev.metallurgists.rutile.api.dynamic_pack.RutilePackSource;
import dev.metallurgists.rutile.api.dynamic_pack.asset.RutileDynamicResourcePack;
import dev.metallurgists.rutile.api.dynamic_pack.data.RutileDynamicDataPack;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.registry.block.RutileMaterialBlocks;
import dev.metallurgists.rutile.api.material.registry.fluid.RutileMaterialFluids;
import dev.metallurgists.rutile.api.material.registry.item.RutileMaterialItems;
import dev.metallurgists.rutile.api.plugin.IRutilePlugin;
import dev.metallurgists.rutile.api.plugin.RutilePluginFinder;
import dev.metallurgists.rutile.api.registrate.MaterialLangGenerator;
import dev.metallurgists.rutile.api.registrate.RutileRegistrate;
import dev.metallurgists.rutile.api.registry.CustomRutileRegistries;
import dev.metallurgists.rutile.api.registry.RutileAPI;
import dev.metallurgists.rutile.config.RutileConfig;
import dev.metallurgists.rutile.mixin.registrate.AbstractRegistrateAccessor;
import dev.metallurgists.rutile.registry.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.ModifyRegistriesEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.neoforged.neoforge.registries.callback.BakeCallback;

import java.util.List;


public class CommonInit {

    private static IEventBus modBus;

    public static void init(final IEventBus modBus) {
        CommonInit.modBus = modBus;
        ModLoadingContext modLoadingContext = ModLoadingContext.get();

        RutileConfig.register(modLoadingContext);
        modBus.addListener(RutileDataGen::gatherDataEvent);
        modBus.register(CommonInit.class);
        CustomRutileRegistries.init(modBus);
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
        RutileMaterials.init();
        RutileFlagKeys.init();

        RutileBlocks.register();
        RutileFluids.register();

        RutileItems.register();
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onRegisterEarly(RegisterEvent event) {
        if (event.getRegistryKey() == CustomRutileRegistries.MATERIAL_REGISTRY) {
            Rutile.LOGGER.debug("Registered {} materials", event.getRegistry().stream().count());
            RutilePluginFinder.getModPlugins().forEach(IRutilePlugin::modifyMaterials);
        } else if (event.getRegistryKey() == Registries.FLUID) {
            // Material fluids
            RutileMaterialFluids.generateMaterialFluids();
            // --spacer--
        } else if (event.getRegistryKey() == Registries.BLOCK) {
            RutileMaterialBlocks.generateMaterialBlocks();
            RutileMaterialBlocks.buildMaterialBlockTable();
        } else if (event.getRegistryKey() == Registries.ITEM) {
            RutileMaterialItems.generateMaterialItems();
        }
    }

    private static void postInitMaterials() {
        // Register all material manager registries, for materials with mod ids.
        RutilePluginFinder.getModPlugins().stream().map(IRutilePlugin::getPluginNamespace).forEach(namespace -> {
            // Force the material lang generator to be at index 0, so that addons' lang generators can override it.
            RutileRegistrate registrate = RutileRegistrate.createIgnoringListenerErrors(namespace);
            AbstractRegistrateAccessor accessor = (AbstractRegistrateAccessor) registrate;
            if (accessor.getDoDatagen().get()) {
                List<NonNullConsumer<? extends RegistrateProvider>> providers = Multimaps.asMap(accessor.getDatagens())
                        .get(ProviderType.LANG);
                if (providers != null)
                    providers.addFirst(
                        (provider) -> MaterialLangGenerator.generate((RegistrateLangProvider) provider, namespace));
            }

            ModList.get().getModContainerById(namespace)
                    .map(ModContainer::getEventBus)
                    .ifPresent(registrate::registerEventListeners);
        });
    }

    @SubscribeEvent
    public static void registerRegistries(NewRegistryEvent event) {
        CustomRutileRegistries.getRegistries().forEach(event::register);
    }

    @SubscribeEvent
    public void newDatapackRegistry(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(CustomRutileRegistries.ITEM_COMPOSITION_REGISTRY, ItemComposition.CODEC, ItemComposition.CODEC);
        event.dataPackRegistry(CustomRutileRegistries.MATERIAL_COMPOSITION_REGISTRY, MaterialComposition.CODEC, MaterialComposition.CODEC);
    }

    @SuppressWarnings("UnstableApiUsage")
    @SubscribeEvent
    public static void modifyRegistries(ModifyRegistriesEvent event) {
        RutileAPI.materialRegistry.addCallback((BakeCallback<Material>) registry -> postInitMaterials());
    }

    @SubscribeEvent
    public static void loadComplete(FMLLoadCompleteEvent event) {}

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
