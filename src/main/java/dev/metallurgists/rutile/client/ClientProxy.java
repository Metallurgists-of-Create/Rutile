package dev.metallurgists.rutile.client;

import dev.metallurgists.rutile.RutileClient;
import dev.metallurgists.rutile.common.CommonProxy;
import dev.metallurgists.rutile.registry.RutilePartialModels;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ClientProxy extends CommonProxy {

    public ClientProxy() {
        super();
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

        init();
        RutilePartialModels.clientInit();
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> RutileClient.onCtor(modEventBus, forgeEventBus));

    }

    public static void init() {

    }
}
