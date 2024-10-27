import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Objects;
import javax.imageio.ImageIO;

public class AssetLoader {
    private static final HashMap<String, BufferedImage> IMAGES = new HashMap<>();
    private static final boolean IS_JAR;

    static {
        IS_JAR = Objects.requireNonNull(AssetLoader.class.getResource("AssetLoader.class"))
                .toString().startsWith("jar:");
    }

    /**
     * Loads an image from the jar file or from the disk.
     * @param path The path to the image
     * @return An image if it was found, or null.
     */
    public static BufferedImage loadImage(String path) {
        if (IMAGES.containsKey(path)) {
            return IMAGES.get(path);
        }

        try {
            BufferedImage image;
            if (IS_JAR) {
                image = ImageIO.read(
                        Objects.requireNonNull(AssetLoader.class.getResourceAsStream(path)));
            } else {
                image = ImageIO.read(new File(path));
            }
            IMAGES.put(path, image);
            return image;
        } catch (IOException e) {
            return null;
        }
    }

    public static InputStream loadStream(String path) {
        try {
            if (IS_JAR) {
                return AssetLoader.class.getResourceAsStream(path);
            } else {
                return new FileInputStream(path);
            }
        } catch (IOException e) {
            return null;
        }
    }
}
