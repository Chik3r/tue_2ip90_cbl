import java.awt.Color;
import java.util.ArrayList;

/**
 * Peg. But *rectangular*!
 */
public class RectPeg extends Peg {
    int width;
    int height;
    double angle;
    
    public RectPeg(int x, int y, int width, int height, boolean orange) {
        this.pos = new Vector2d(x, y);
        this.width = width;
        this.height = height;
        this.orange = orange;
        this.beenHit = false;
        this.angle = 0;

        ArrayList<Vector2d> vertices = new ArrayList<Vector2d>();
        vertices.add(pos);
        vertices.add(new Vector2d(x + width, y));
        vertices.add(new Vector2d(x + width, y + height));
        vertices.add(new Vector2d(x, y + height));
        
        this.collider = new PolygonCollider(vertices);
    }

    public RectPeg(int x1, int y1, int x2, int y2, int height, boolean orange) {
        var point1 = new Vector2d(x1, y1);
        var point2 = new Vector2d(x2, y2);
        var point3 = point2.add(point2.subtract(point1).normal().unit().scalarMult(height));

        System.out.println(point1);
        System.out.println(point2);
        System.out.println(point3);

        this.width = (int) point2.subtract(point1).length();
        this.height = height;
        this.orange = orange;
        this.beenHit = false;
        this.angle = point2.subtract(point1).angle();
        this.pos = point3.subtract(point1).scalarMult(0.5).add(new Vector2d(-width / 2,-height / 2));

        System.out.println(angle);
        System.out.println(pos);

        ArrayList<Vector2d> vertices = new ArrayList<Vector2d>();
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

    public RectPeg(int x, int y, int width, int height, boolean orange, double angle) {
        this.pos = new Vector2d(x, y);
        this.width = width;
        this.height = height;
        this.orange = orange;
        this.beenHit = false;
        this.angle = angle;


        ArrayList<Vector2d> vertices = new ArrayList<Vector2d>();
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
        Color colour = (orange) ? new Color(255, 153, 51) : Color.blue;
        g.fillRotatedRect((int) pos.x, (int) pos.y, width, height, angle, colour);
        colour = (orange) ? Color.orange : Color.cyan; 
        g.fillRotatedRect((int) pos.x + 10, (int) pos.y + 10, width - 20, height - 20, angle, colour);
        if (beenHit) {
            // TODO: fancy particles :D
            // make it glowy
        }
    }

    // @Override
    // public void draw(GraphicsWrapper g) {
        // Color colour = (orange) ? new Color(255, 153, 51) : Color.blue; 
        // g.fillRect((int) pos.x, (int) pos.y, width, height, colour);
        // colour = (orange) ? Color.orange : Color.cyan; 
        // g.fillRect((int) pos.x + 10, (int) pos.y + 10, width - 20, height - 20, colour);
        // if (beenHit) {
            // TODO: fancy particles :D
            // make it glowy
        // }
    // }

    @Override
    public void update(float deltaTime) {

    }
}
