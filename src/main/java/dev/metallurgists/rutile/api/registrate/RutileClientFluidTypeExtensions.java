package dev.metallurgists.rutile.api.registrate;

import dev.metallurgists.rutile.Rutile;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import org.jetbrains.annotations.Nullable;

public class RutileClientFluidTypeExtensions implements IClientFluidTypeExtensions {
    public static final ResourceLocation FLUID_SCREEN_OVERLAY = Rutile.id("textures/misc/fluid_screen_overlay.png");

    public RutileClientFluidTypeExtensions(@Nullable ResourceLocation stillTexture,
                                       @Nullable ResourceLocation flowingTexture,
                                       int tintColor) {
        this.stillTexture = stillTexture;
        this.flowingTexture = flowingTexture;
        this.tintColor = tintColor;
    }

    @Getter
    @Setter
    @Nullable
    private ResourceLocation flowingTexture, stillTexture;
    @Getter
    @Setter
    private int tintColor;

    @Override
    public ResourceLocation getRenderOverlayTexture(Minecraft mc) {
        return FLUID_SCREEN_OVERLAY;
    }
}
