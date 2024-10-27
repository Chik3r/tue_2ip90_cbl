import java.awt.*;

public class ColorUtil {
    /**
     * Linearly interpolates between two colors.
     */
    public static Color colorLerp(Color color1, Color color2, float f) {
        return new Color(
                (int) lerp(color1.getRed(), color2.getRed(), f),
                (int) lerp(color1.getGreen(), color2.getGreen(), f),
                (int) lerp(color1.getBlue(), color2.getBlue(), f),
                color1.getAlpha());
    }

    public static float lerp(float a, float b, float f) {
        return a * (1.0f - f) + (b * f);
    }
}
