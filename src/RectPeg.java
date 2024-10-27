import java.awt.Color;
import java.util.ArrayList;

/**
 * Peg. But *rectangular*!
 */
public class RectPeg extends Peg {
    final int width;
    final int height;
    final double angle;
    
    public RectPeg(int x, int y, int width, int height, boolean orange) {
        this.pos = new Vector2d(x, y);
        this.width = width;
        this.height = height;
        this.orange = orange;
        this.beenHit = false;
        this.angle = 0;

        ArrayList<Vector2d> vertices = new ArrayList<>();
        vertices.add(pos);
        vertices.add(new Vector2d(x + width, y));
        vertices.add(new Vector2d(x + width, y + height));
        vertices.add(new Vector2d(x, y + height));
        
        this.collider = new PolygonCollider(vertices);
    }

    public RectPeg(int x1, int y1, int x2, int y2, int height, boolean orange) {
        this(x1, y1,
             (int) new Vector2d(x1, y1).subtract(new Vector2d(x2, y2)).length(), 
             height, 
             orange,
             new Vector2d(x2, y2).subtract(new Vector2d(x1, y1)).angle());

        var vertex1 = ((PolygonCollider) collider).getWorldVertices().get(0);
        pos = pos.add(pos.subtract(vertex1));
        collider.setWorldPos(collider.getWorldCenter().add(pos.subtract(vertex1).scalarMult(0.5)));
        
    }

    public RectPeg(int x, int y, int width, int height, boolean orange, double angle) {
        this.pos = new Vector2d(x, y);
        this.width = width;
        this.height = height;
        this.orange = orange;
        this.beenHit = false;
        this.angle = angle;


        ArrayList<Vector2d> vertices = new ArrayList<>();
        double tmpX = width / 2.0;
        double tmpY = height / 2.0;
        vertices.add(new Vector2d(-tmpX, -tmpY));
        vertices.add(new Vector2d(tmpX, -tmpY));
        vertices.add(new Vector2d(tmpX, tmpY));
        vertices.add(new Vector2d(-tmpX, tmpY));

        var col = new PolygonCollider(vertices);
        col.setWorldPos(pos.add(new Vector2d(tmpX, tmpY)));
        col.setAngle(angle);
        this.collider = col;
    }

    @Override
    public void draw(GraphicsWrapper g) {
        g.fillRotatedRect((int) pos.x, (int) pos.y, width, height, angle, determineOuterColor());
        g.fillRotatedRect((int) pos.x + 10, (int) pos.y + 10,
                width - 20, height - 20, angle, determineInnerColor());
    }

    @Override
    public void update(float deltaTime) {

    }
}
