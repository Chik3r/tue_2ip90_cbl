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

    public void drawLine(int x1, int y1, int x2, int y2, Color color) {
        setColor(color);
        graphics.drawLine(x1 + bounds.x, y1 + bounds.y, x2 + bounds.x, y2 + bounds.y);
    }

    public void drawImage(Image image, int x, int y, int width, int height) {
        graphics.drawImage(image, x + bounds.x, y + bounds.y, width, height, null);
    }
}
