import java.awt.*;
import java.util.ArrayList;

public class BorderCollider extends Entity {
    private final int WIDTH = 50;

    private PolygonCollider colliderL;
    private PolygonCollider colliderU;
    private PolygonCollider colliderR;

    private ArrayList<PolygonCollider> colliders;
    
    public BorderCollider(Rectangle borders) {

        ArrayList<Vector2d> verticesL = new ArrayList<Vector2d>();
        verticesL.add(new Vector2d(borders.x - WIDTH, borders.y));
        verticesL.add(new Vector2d(borders.x, borders.y));
        verticesL.add(new Vector2d(borders.x, borders.y + borders.getHeight()));
        verticesL.add(new Vector2d(borders.x - WIDTH, borders.y + borders.getHeight()));
        colliderL = new PolygonCollider(verticesL);

        ArrayList<Vector2d> verticesU = new ArrayList<Vector2d>();
        verticesU.add(new Vector2d(borders.x - WIDTH, borders.y - WIDTH));
        verticesU.add(new Vector2d(borders.x + borders.getWidth() + WIDTH, borders.y - WIDTH));
        verticesU.add(new Vector2d(borders.x + borders.getWidth() + WIDTH, borders.y));
        verticesU.add(new Vector2d(borders.x - WIDTH, borders.y));
        colliderU = new PolygonCollider(verticesU);

        ArrayList<Vector2d> verticesR = new ArrayList<Vector2d>();
        verticesR.add(new Vector2d(borders.x + borders.getWidth(), borders.y));
        verticesR.add(new Vector2d(borders.x + borders.getWidth() + WIDTH, borders.y));
        verticesR.add(new Vector2d(borders.x + borders.getWidth() + WIDTH, borders.y + borders.getHeight()));
        verticesR.add(new Vector2d(borders.x + borders.getWidth(), borders.y + borders.getHeight()));
        colliderR = new PolygonCollider(verticesR);

        ArrayList<PolygonCollider> colliders = new ArrayList<>();
        colliders.add(colliderL);
        colliders.add(colliderU);
        colliders.add(colliderR);
        this.colliders = colliders;
    }

    @Override
    public void draw(GraphicsWrapper g) {

    }

    @Override
    public void update(float deltaTime) {

    }

    public ArrayList<PolygonCollider> getColliders() {
        return colliders;
    }
}
