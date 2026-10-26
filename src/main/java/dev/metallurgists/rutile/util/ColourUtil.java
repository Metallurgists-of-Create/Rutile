package dev.metallurgists.rutile.util;

import java.awt.*;
import java.util.stream.Stream;

public class ColourUtil {

    public static int blendAll(Stream<Integer> colors) {
		if (colors == null) return 0xFF000000;
		return colors.reduce(
			new ColorBlend(),
			ColorBlend::addColor,
			ColorBlend::blend
		).getColor();
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
	
	private record ColorBlend (int count, long a, long r, long g, long b) {
		public ColorBlend() {
			this(0, 0, 0, 0, 0);
		}
		
		public ColorBlend addColor(int color) {
			int a = (color >> 24) & 0xFF;
			int r = (color >> 16) & 0xFF;
			int g = (color >> 8) & 0xFF;
			int b = color & 0xFF;
			return addColor(a, r, g, b);
		}
		
		public ColorBlend addColor(int a, int r, int g, int b) {
			return new ColorBlend(
				count + 1,
				this.a + a,
				this.r + (long) a * r,
				this.g + (long) a * g,
				this.b + (long) a * b
			);
		}
		
		public ColorBlend blend(ColorBlend other) {
			return new ColorBlend(
				count + other.count,
				a + other.a,
				r + other.r,
				g + other.g,
				b + other.b
			);
		}
		
		public int getColor() {
			if (a == 0) return 0xFF000000;
			int avgA = (int)(a / count);
			int avgR = (int)(r / a);
			int avgG = (int)(g / a);
			int avgB = (int)(b / a);
			return (avgA << 24) | (avgR << 16) | (avgG << 8) | avgB;
		}
	}
}
