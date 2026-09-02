package dev.metallurgists.rutile.util;

import java.awt.*;
import java.util.List;

public class ColourUtil {

    public static int blendAll(List<Integer> colors) {
        if (colors == null || colors.isEmpty()) {
            return 0xFF000000;
        }
        long totalA = 0, totalR = 0, totalG = 0, totalB = 0;

        for (int color : colors) {
            int a = (color >> 24) & 0xFF;
            int r = (color >> 16) & 0xFF;
            int g = (color >> 8) & 0xFF;
            int b = color & 0xFF;
            totalA += a;
            totalR += r * a;
            totalG += g * a;
            totalB += b * a;
        }

        if (totalA == 0) return 0xFF000000;
        int avgA = (int)(totalA / colors.size());
        int avgR = (int)(totalR / totalA);
        int avgG = (int)(totalG / totalA);
        int avgB = (int)(totalB / totalA);
        return (avgA << 24) | (avgR << 16) | (avgG << 8) | avgB;
    }

    public static int brighter(int rgba, float maxSaturation) {
        Color c = new Color(rgba, true);
        int alpha = c.getAlpha();
        float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
        float FACTOR = 0.7f;
        float newBrightness = Math.min(1.0f, hsb[2] / FACTOR);
        float newSaturation = Math.clamp(maxSaturation, 0f, hsb[1]);
        int rgbNoAlpha = Color.HSBtoRGB(hsb[0], newSaturation, newBrightness) & 0x00FFFFFF;
        return (alpha << 24) | rgbNoAlpha;
    }
}
