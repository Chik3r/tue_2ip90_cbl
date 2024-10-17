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
        //this.collider = new collider(x,y,radius)
    }

    @Override
    public void draw(GraphicsWrapper g) {
        Color colour = (orange) ? new Color(255, 153, 51) : Color.cyan; 
        g.fillOval((int) pos.x, (int) pos.y, radius, radius, colour);
        colour = (orange) ? Color.orange : Color.blue; 
        g.fillOval((int) pos.x, (int) pos.y, radius * - 5, radius - 5, colour);
        if (beenHit) {
            // TODO: fancy particles :D
            // make it glowy
        }
    }

    @Override
    public void update(float deltaTime) {

    }
}
