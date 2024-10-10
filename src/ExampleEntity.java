import java.awt.*;

public class ExampleEntity extends Entity {
    int xPos = 0;

    @Override
    public void draw(float deltaTime, Graphics g, Rectangle bounds) {
//        // Example drawing
//        g.setColor(Color.gray);
//        g.drawString("Hello world!", frameBounds.x, frameBounds.y + 20);
//        double v = Math.sin(System.currentTimeMillis() / 1000.0) + 1;
//        g.fillRect(frameBounds.x, frameBounds.y, (int) (v * 100), (int) (v * 100));

        g.setColor(Color.pink);
        // TODO: Wrapper class for Graphics that automatically adds the offset for the top bar of the window
        g.fillOval(xPos + bounds.x, bounds.y + 200, 75, 75);
    }

    @Override
    public void update(float deltaTime) {
        xPos += (int) (0.1f*deltaTime);
    }
}
