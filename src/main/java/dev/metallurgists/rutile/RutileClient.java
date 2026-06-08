package dev.metallurgists.rutile;


import dev.metallurgists.rutile.api.composition.CompositionHandler;
import net.createmod.catnip.lang.LangBuilder;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

public class RutileClient {

    public static void init() {
    }

    public static LangBuilder getLang() {
        return new LangBuilder(Rutile.ID);
    }

    @EventBusSubscriber(Dist.CLIENT)
    public static class ClientEvents {
        @SubscribeEvent
        public static void onItemTooltip(ItemTooltipEvent event) {
            CompositionHandler.addToTooltip(event.getToolTip(), event.getItemStack(), event.getContext().registries());
        }

        @SubscribeEvent
        static void onRegisterReloadListeners(RegisterClientReloadListenersEvent event) {

        }
    }
}
