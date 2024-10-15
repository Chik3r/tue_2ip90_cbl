import java.awt.*;

public abstract class Entity {
    public abstract void draw(GraphicsWrapper g, Rectangle bounds);
    public abstract void update(float deltaTime);
}
