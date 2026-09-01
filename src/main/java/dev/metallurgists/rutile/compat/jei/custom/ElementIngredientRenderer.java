package dev.metallurgists.rutile.compat.jei.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.composition.element.Element;
import dev.metallurgists.rutile.api.composition.element.ElementStack;
import lombok.RequiredArgsConstructor;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ParametersAreNonnullByDefault
@RequiredArgsConstructor
public class ElementIngredientRenderer implements IIngredientRenderer<ElementStack> {
    private final int size;

    @Override
    public int getWidth() {
        return size;
    }

    @Override
    public int getHeight() {
        return size;
    }

    @Override
    public void render(GuiGraphics guiGraphics, ElementStack element) {
        render(guiGraphics, element, 0, 0);
    }

    @Override
    public void render(GuiGraphics guiGraphics, ElementStack element, int posX, int posY) {
        RenderSystem.enableBlend();
        drawPlaque(guiGraphics, element.getElement(), posX, posY);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.disableBlend();
        RenderSystem.enableDepthTest();
        Minecraft minecraft = Minecraft.getInstance();
        Font font = getFontRenderer(minecraft, element);
        guiGraphics.pose().pushPose();
        String symbol = element.getElement().getSymbol();
        guiGraphics.pose().translate(0, 0, 200.0F);
        float scale = 0.8f;
        guiGraphics.pose().translate(posX, posY, 0);
        guiGraphics.pose().scale(scale, scale, scale);
        int x = Math.round((size / (2f * scale)) - font.width(symbol) / 2f);
        int y = Math.round((size / (2f * scale)) - font.lineHeight / 2f);
        guiGraphics.drawString(font, symbol, x, y, element.getColor(), true);
        guiGraphics.pose().popPose();
    }

    private void drawPlaque(GuiGraphics guiGraphics, Element element, int posX, int posY) {
        if (element.getId().equals(Rutile.id("null"))) {
            getNullPlaqueSprite().ifPresent(plaqueSprite -> {
                RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
                Matrix4f matrix = guiGraphics.pose().last().pose();
                drawTextureWithMasking(matrix, posX, posY, plaqueSprite, 0, 0, 100);
            });
        } else {
            getPlaqueSprite().ifPresent(plaqueSprite -> {
                int plaqueColor = new Color(element.getColor(), true).getRGB();
                RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
                Matrix4f matrix = guiGraphics.pose().last().pose();
                setGLColorFromInt(plaqueColor);
                drawTextureWithMasking(matrix, posX, posY, plaqueSprite, 0, 0, 100);
            });
        }
    }

    public Optional<TextureAtlasSprite> getPlaqueSprite() {
        return Optional.ofNullable(Minecraft.getInstance()
                        .getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
                        .apply(Rutile.id("element/plaque")))
                .filter(s -> s.atlasLocation() != MissingTextureAtlasSprite.getLocation());
    }

    public Optional<TextureAtlasSprite> getNullPlaqueSprite() {
        return Optional.ofNullable(Minecraft.getInstance()
                        .getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
                        .apply(Rutile.id("element/null_plaque")))
                .filter(s -> s.atlasLocation() != MissingTextureAtlasSprite.getLocation());
    }

    private static void setGLColorFromInt(int color) {
        float red = (color >> 16 & 0xFF) / 255.0F;
        float green = (color >> 8 & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;
        float alpha = ((color >> 24) & 0xFF) / 255F;

        RenderSystem.setShaderColor(red, green, blue, alpha);
    }

    private static void drawTextureWithMasking(Matrix4f matrix, float xCoord, float yCoord, TextureAtlasSprite textureSprite, long maskTop, long maskRight, float zLevel) {
        float uMin = textureSprite.getU0();
        float uMax = textureSprite.getU1();
        float vMin = textureSprite.getV0();
        float vMax = textureSprite.getV1();
        uMax = uMax - (maskRight / 16F * (uMax - uMin));
        vMax = vMax - (maskTop / 16F * (vMax - vMin));

        RenderSystem.setShader(GameRenderer::getPositionTexShader);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferBuilder.addVertex(matrix, xCoord, yCoord + 16, zLevel).setUv(uMin, vMax);
        bufferBuilder.addVertex(matrix, xCoord + 16 - maskRight, yCoord + 16, zLevel).setUv(uMax, vMax);
        bufferBuilder.addVertex(matrix, xCoord + 16 - maskRight, yCoord + maskTop, zLevel).setUv(uMax, vMin);
        bufferBuilder.addVertex(matrix, xCoord, yCoord + maskTop, zLevel).setUv(uMin, vMin);
        BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
    }

    @Override
    public @NotNull List<Component> getTooltip(ElementStack element, TooltipFlag flag) {
        List<Component> tooltip = new ArrayList<>();
        tooltip.add(element.getElement().getDisplayName());
        if (flag.isAdvanced()) {
            tooltip.add((Component.literal(element.getId().toString())).withStyle(ChatFormatting.DARK_GRAY));
        }
        return tooltip;
    }
}
