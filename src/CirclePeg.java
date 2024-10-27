import java.awt.Color;

/**
 * Peg. But *circular*!
 */
public class CirclePeg extends Peg {
    final int radius;
        
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
        g.fillOval((int) pos.x, (int) pos.y, radius * 2, radius * 2, determineOuterColor());
        g.fillOval(((int) pos.x) + 10, ((int) pos.y) + 10,
                ((radius - 10) * 2), ((radius - 10) * 2), determineInnerColor());
    }

    @Override
    public void update(float deltaTime) {

    }
}
