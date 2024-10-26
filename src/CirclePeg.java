import java.awt.Color;

/**
 * Peg. But *circular*!
 */
public class CirclePeg extends Peg {
    int radius;
        
    public CirclePeg(int x, int y, int radius, boolean orange) {
        this.pos = new Vector2d(x, y);
        this.radius = radius;
        this.orange = orange;
        this.beenHit = false;

        Vector2d center = new Vector2d(radius, radius);
        this.collider = new CircleCollider(center, pos, radius);
    }

    @Override
    public void draw(GraphicsWrapper g) {
        Color colour = (orange) ? new Color(255, 153, 51) : Color.blue; 
        g.fillOval((int) pos.x, (int) pos.y, radius * 2, radius * 2, colour);
        colour = (orange) ? Color.orange : Color.cyan; 
        g.fillOval(((int) pos.x) + 10, ((int) pos.y) + 10, ((radius - 10) * 2), ((radius - 10) * 2), colour);
        if (beenHit) {
            // TODO: fancy particles :D
            // make it glowy
        }
    }

    @Override
    public void update(float deltaTime) {

    }

    public void updateCollider() {
        Vector2d center = new Vector2d(radius, radius);
        this.collider = new CircleCollider(center, pos, radius);
    }
}
