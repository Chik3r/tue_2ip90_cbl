import java.awt.Color;
import java.util.ArrayList;

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

        ArrayList<Vector2d> vertices = new ArrayList<Vector2d>();
        vertices.add(pos);
        vertices.add(new Vector2d(x + width, y));
        vertices.add(new Vector2d(x + width, y + height));
        vertices.add(new Vector2d(x, y + height));
        
        this.collider = new PolygonCollider(vertices);
    }

    @Override
    public void draw(GraphicsWrapper g) {
        Color colour = (orange) ? new Color(255, 153, 51) : Color.blue; 
        g.fillRect((int) pos.x, (int) pos.y, width, height, colour);
        colour = (orange) ? Color.orange : Color.cyan; 
        g.fillRect((int) pos.x + 10, (int) pos.y + 10, width - 20, height - 20, colour);
        if (beenHit) {
            // TODO: fancy particles :D
            // make it glowy
        }
    }

    @Override
    public void update(float deltaTime) {

    }
}
