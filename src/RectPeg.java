import java.awt.Color;

/**
 * Peg. But *rectangular*!
 */
public class RectPeg extends Peg {
    int width;
    int height;
    
    public RectPeg(int x, int y, int width, int height, boolean orange) {
        this.pos = new Vector2d(x, y);
        this.width = width;
        this.height = height;
        this.orange = orange;
        this.beenHit = false;
        //this.collider = new collider
    }

    @Override
    public void draw(GraphicsWrapper g) {
        Color colour = (orange) ? new Color(255, 153, 51) : Color.cyan; 
        g.fillOval((int) pos.x, (int) pos.y, width, height, colour);
        colour = (orange) ? Color.orange : Color.blue; 
        g.fillOval((int) pos.x, (int) pos.y, width, height, colour);
        if (beenHit) {
            // TODO: fancy particles :D
            // make it glowy
        }
    }

    @Override
    public void update(float deltaTime) {

    }
}
