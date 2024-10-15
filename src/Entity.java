import java.awt.*;

public abstract class Entity {
    public abstract void draw(GraphicsWrapper g);
    public abstract void update(float deltaTime);
}
