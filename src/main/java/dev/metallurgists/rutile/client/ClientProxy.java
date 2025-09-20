package dev.metallurgists.rutile.client;

import com.tterrag.registrate.util.RegistrateDistExecutor;
import dev.metallurgists.rutile.RutileClient;
import dev.metallurgists.rutile.common.CommonInit;
import dev.metallurgists.rutile.registry.RutilePartialModels;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.neoforge.common.NeoForge;


public class ClientProxy extends CommonInit {

    public ClientProxy() {
        super();
        IEventBus modEventBus = ModLoadingContext.get().getActiveContainer().getEventBus();
        IEventBus forgeEventBus = NeoForge.EVENT_BUS;

        init();
        RutilePartialModels.clientInit();
        RegistrateDistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> RutileClient.onCtor(modEventBus, forgeEventBus));

    }

    public static void init() {

    }
}
