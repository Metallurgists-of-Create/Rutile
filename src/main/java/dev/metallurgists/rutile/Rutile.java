package dev.metallurgists.rutile;

import dev.metallurgists.rutile.api.plugin.PluginRegistry;
import dev.metallurgists.rutile.config.RutileConfig;
import dev.metallurgists.rutile.datagen.RutileDataGen;
import dev.metallurgists.rutile.events.CommonEvents;
import dev.metallurgists.rutile.registry.RutileElements;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Contract;

import java.nio.file.Path;


@Mod(Rutile.ID)
public class Rutile {
    private static Rutile INSTANCE;
    public static final String ID = "rutile";
    public static final String DISPLAY_NAME = "Rutile";
    public static final Logger LOGGER = LogManager.getLogger();

    @Getter
    private static final RutileRegistrate registrate = RutileRegistrate.create(ID);

    private final IEventBus modEventBus;

    public Rutile(IEventBus modEventBus, ModContainer modContainer) {
        this.modEventBus = modEventBus;
        ModLoadingContext modLoadingContext = ModLoadingContext.get();
        INSTANCE = this;

        RutileConfig.register(modLoadingContext);
        CommonEvents.init(INSTANCE.modEventBus);
        PluginRegistry.getInstance().loadPlugins();

        modEventBus.addListener(RutileDataGen::gatherDataEvent);
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

    public static boolean isDev() {
        return !FMLEnvironment.production;
    }

    @Contract("_ -> new")
    public static ResourceLocation id(String path) {
        if (path.contains(":")) {
            return ResourceLocation.tryParse(path);
        }
        return ResourceLocation.fromNamespaceAndPath(ID, path);
    }

}
