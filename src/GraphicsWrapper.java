import java.awt.*;

public class GraphicsWrapper {
    private final Graphics2D graphics;
    private final Rectangle bounds;

    public GraphicsWrapper(Graphics2D graphics, Rectangle bounds) {
        this.graphics = graphics;
        this.bounds = bounds;
    }

    public void setColor(Color color) {
        graphics.setColor(color);
    }

    public void fillOval(int x, int y, int width, int height) {
        graphics.fillOval(x + bounds.x, y + bounds.y, width, height);
    }

    public void fillOval(int x, int y, int width, int height, Color color) {
        setColor(color);
        fillOval(x, y, width, height);
    }

    public void fillRect(int x, int y, int width, int height) {
        graphics.fillRect(x + bounds.x, y + bounds.y, width, height);
    }

    public void fillRect(int x, int y, int width, int height, Color color) {
        setColor(color);
        fillRect(x, y, width, height);
    }

    public void drawImage(Image image, int x, int y, int width, int height) {
        graphics.drawImage(image, x + bounds.x, y + bounds.y, width, height, null);
    }
}
