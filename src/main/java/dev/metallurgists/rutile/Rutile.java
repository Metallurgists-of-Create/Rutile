package dev.metallurgists.rutile;

import com.mojang.logging.LogUtils;
import dev.metallurgists.rutile.api.material.events.MaterialEvent;
import dev.metallurgists.rutile.api.material.events.PostMaterialEvent;
import dev.metallurgists.rutile.api.plugin.IRutilePlugin;
import dev.metallurgists.rutile.api.plugin.RutilePluginFinder;
import dev.metallurgists.rutile.api.registrate.RutileRegistrate;
import dev.metallurgists.rutile.api.registry.RutileAPI;
import dev.metallurgists.rutile.client.ClientProxy;
import dev.metallurgists.rutile.common.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
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
        Rutile.init();
        RutileAPI.instance = this;
        var bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.register(this);
        DistExecutor.unsafeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
    }

    public static void init() {
        LOGGER.info("{} is initializing...", DISPLAY_NAME);
    }

    @SubscribeEvent
    public void modifyMaterials(PostMaterialEvent event) {
        RutilePluginFinder.getModPlugins().forEach(IRutilePlugin::modifyMaterials);
    }

    @SubscribeEvent
    public void registerMaterials(MaterialEvent event) {
        RutilePluginFinder.getModPlugins().forEach(IRutilePlugin::registerMaterials);
    }

    public static ResourceLocation id(String path) {
        if (path.contains(":")) {
            return new ResourceLocation(path);
        }
        return new ResourceLocation(ID, path);
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

    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(ID, path);
    }
    public static @NotNull RutileRegistrate registrate() {
        return registrate;
    }

}
