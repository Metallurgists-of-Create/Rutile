package dev.metallurgists.rutile.mixin.jei;

import dev.metallurgists.rutile.api.composition.CompositionHandler;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.neoforge.platform.FluidHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = FluidHelper.class, remap = false)
public class FluidHelperMixin {
    @Inject(method = "getTooltip(Lmezz/jei/api/gui/builder/ITooltipBuilder;Lnet/neoforged/neoforge/fluids/FluidStack;Lnet/minecraft/world/item/TooltipFlag;)V",
            at = @At("TAIL"),
            remap = false,
            require = 0)
    private void gtceu$injectFluidTooltips(ITooltipBuilder tooltip, FluidStack ingredient, TooltipFlag tooltipFlag,
                                           CallbackInfo ci) {
        CompositionHandler.appendFluidTooltips(ingredient, tooltip::add);
    }
}
