package dev.metallurgists.rutile;

import dev.metallurgists.rutile.api.composition.CompositionTooltipHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class RutileClient {
    public static void onCtor(IEventBus modEventBus, IEventBus forgeEventBus) {

    }

    @Mod.EventBusSubscriber(Dist.CLIENT)
    public static class ClientEvents {
        @SubscribeEvent
        public static void onItemTooltip(ItemTooltipEvent event) {
            CompositionTooltipHandler.addToTooltip(event.getToolTip(), event.getItemStack());
        }
    }
}
