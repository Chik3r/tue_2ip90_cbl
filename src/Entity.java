import java.awt.*;

public abstract class Entity {
    public abstract void draw(float deltaTime, GraphicsWrapper g);
    public abstract void update(float deltaTime);
}
