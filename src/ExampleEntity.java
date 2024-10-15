import java.awt.*;

public class ExampleEntity extends Entity {
    int xPos = 0;

    @Override
    public void draw(GraphicsWrapper g) {
        g.fillOval(xPos, 200, 75, 75, Color.pink);
    }

    @Override
    public void update(float deltaTime) {
        xPos += (int) (0.1f*deltaTime);
    }
}
