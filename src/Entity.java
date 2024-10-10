import java.awt.*;

public abstract class Entity {
    public abstract void draw(float deltaTime, Graphics g, Rectangle bounds);
    public abstract void update(float deltaTime);
}
