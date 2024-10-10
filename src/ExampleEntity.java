import java.awt.*;

public class ExampleEntity extends Entity {
    int xPos = 0;

    @Override
    public void draw(float deltaTime, GraphicsWrapper g) {
//        // Example drawing
//        g.setColor(Color.gray);
//        g.drawString("Hello world!", frameBounds.x, frameBounds.y + 20);
//        double v = Math.sin(System.currentTimeMillis() / 1000.0) + 1;
//        g.fillRect(frameBounds.x, frameBounds.y, (int) (v * 100), (int) (v * 100));

        g.fillOval(xPos, 200, 75, 75, Color.pink);
    }

    @Override
    public void update(float deltaTime) {
        xPos += (int) (0.1f*deltaTime);
    }
}
