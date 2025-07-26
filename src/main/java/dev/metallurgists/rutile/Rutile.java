package dev.metallurgists.rutile;

import com.mojang.logging.LogUtils;
import dev.metallurgists.rutile.api.plugin.IRutilePlugin;
import dev.metallurgists.rutile.api.plugin.RutilePluginFinder;
import dev.metallurgists.rutile.api.registrate.RutileRegistrate;
import dev.metallurgists.rutile.api.registry.RutileRegistries;
import dev.metallurgists.rutile.config.RutileConfig;
import net.createmod.ponder.ForgePonderClient;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.nio.file.Path;


@Mod(Rutile.ID)
public class Rutile {
    public static final String ID = "rutile";
    public static final String DISPLAY_NAME = "Rutile";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final RutileRegistrate registrate = RutileRegistrate.create(ID);

    public Rutile() {
        ModLoadingContext modLoadingContext = ModLoadingContext.get();
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
        registrate.registerEventListeners(modEventBus);

        RutileRegistries.init();
        RutilePluginFinder.getModPlugins().forEach(Rutile::initPlugins);

        RutileConfig.register(modLoadingContext);

        MinecraftForge.EVENT_BUS.register(new EventHandler());
        MinecraftForge.EVENT_BUS.register(new CommonEvents());

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> RutileClient.onCtor(modEventBus, forgeEventBus));

        MinecraftForge.EVENT_BUS.register(this);
    }

    public static void initPlugins(IRutilePlugin plugin) {
        plugin.registerElements();
        plugin.registerFlags();
        plugin.registerMaterials();
        plugin.registerObjects();
    }

    public static Path getGameDir() {
        return FMLPaths.GAMEDIR.get();
    }

    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(ID, path);
    }
    public static @NotNull RutileRegistrate registrate() {
        return registrate;
    }

}
