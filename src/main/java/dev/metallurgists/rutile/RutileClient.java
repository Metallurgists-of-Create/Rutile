package dev.metallurgists.rutile;

import dev.metallurgists.rutile.api.composition.CompositionTooltipHandler;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.Objects;


public class RutileClient {

    @EventBusSubscriber(Dist.CLIENT)
    public static class ClientEvents {
        @SubscribeEvent
        public static void onItemTooltip(ItemTooltipEvent event) {
            CompositionTooltipHandler.addToTooltip(event.getToolTip(), event.getItemStack(), Objects.requireNonNull(event.getContext().registries()));
        }
    }
}
