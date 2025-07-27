package dev.metallurgists.rutile.client;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.composition.CompositionTooltipHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ClientEventListener {

    @SubscribeEvent
    public void onTooltipEvent(ItemTooltipEvent event) {
        CompositionTooltipHandler.addToTooltip(event.getToolTip(), event.getItemStack());
    }
}
