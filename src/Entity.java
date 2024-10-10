import java.awt.*;

public abstract class Entity {
    public abstract void Draw(float deltaTime, Graphics g, Rectangle bounds);
    public abstract void Update(float deltaTime);
}
