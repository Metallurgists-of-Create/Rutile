package dev.metallurgists.rutile;

import com.mojang.logging.LogUtils;
import dev.metallurgists.rutile.api.plugin.PluginRegistry;
import dev.metallurgists.rutile.config.RutileConfig;
import dev.metallurgists.rutile.events.CommonEvents;
import dev.metallurgists.rutile.registry.RutileModules;
import dev.metallurgists.rutile.registry.RutileRegisterKeys;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLPaths;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.nio.file.Path;


@Mod(Rutile.ID)
public class Rutile {
    private static Rutile INSTANCE;
    public static final String ID = "rutile";
    public static final String DISPLAY_NAME = "Rutile";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final RutileRegistrate registrate = RutileRegistrate.create(ID);

    private final IEventBus modEventBus;

    public Rutile(IEventBus modEventBus) throws NoSuchFieldException, IllegalAccessException {
        this.modEventBus = modEventBus;
        ModLoadingContext modLoadingContext = ModLoadingContext.get();
        INSTANCE = this;

        RutileConfig.register(modLoadingContext);
        RutileRegisterKeys.KEYS.register(modEventBus);
        RutileModules.MODULES.register(modEventBus);
        CommonEvents.init(INSTANCE.modEventBus);

        PluginRegistry.getInstance().loadPlugins();

        modEventBus.addListener(RutileDataGen::gatherDataEvent);
    }

    public static void init() {
        LOGGER.info("{} is initializing...", DISPLAY_NAME);
        RutileClient.init();
    }

    @SubscribeEvent
    public void onCommonSetup(FMLCommonSetupEvent event) {

    }


    public static ResourceLocation id(String path) {
        if (path.contains(":")) {
            return ResourceLocation.parse(path);
        }
        return ResourceLocation.fromNamespaceAndPath(ID, path);
    }


    public static IEventBus getEventBus() {
        return INSTANCE.modEventBus;
    }

    public static Path getGameDir() {
        return FMLPaths.GAMEDIR.get();
    }

    public static boolean isClientThread() {
        return isClientSide() && Minecraft.getInstance().isSameThread();
    }

    public static boolean isClientSide() {
        return FMLEnvironment.dist.isClient();
    }

    public static @NotNull RutileRegistrate registrate() {
        return registrate;
    }

    public static boolean isDev() {
        return !FMLEnvironment.production;
    }
}
