import java.awt.*;
import java.util.ArrayList;

public class PolygonCollider extends Collider {
    protected final ArrayList<Vector2d> origVertices;
    protected final ArrayList<Vector2d> worldVertices;
    protected final ArrayList<Vector2d> normals;

    private double angle;

    public PolygonCollider(ArrayList<Vector2d> vertices) {
        this(new Vector2d(0, 0), vertices);
    }

    public PolygonCollider(Vector2d center, ArrayList<Vector2d> vertices) {
        this(center, new Vector2d(0, 0), vertices);
    }

    public PolygonCollider(Vector2d center, Vector2d worldPos, ArrayList<Vector2d> vertices) {
        super(center, worldPos);

        this.origVertices = vertices;
        this.worldVertices = new ArrayList<>();
        this.normals = new ArrayList<>();

        // Normals don't care about the world position of the object
        for (int i = 0; i < vertices.size(); i++) {
            Vector2d v1 = vertices.get(i);
            Vector2d v2 = vertices.get((i + 1) % vertices.size());

            Vector2d line = new Vector2d(v2.x - v1.x, v2.y - v1.y);
            var normal = line.normal();

            // TODO: Optimization: Deduplicate normals.
            //  If two vectors have opposite directions, then we only need to keep one of them.

            normals.add(normal);
        }

        setWorldPos(worldPos);
    }

    @Override
    public void setWorldPos(Vector2d worldPos) {
        super.setWorldPos(worldPos);
        recalculateWorldVertices();
    }

    public ArrayList<Vector2d> getWorldVertices() {
        return worldVertices;
    }

    public void setAngle(double angle) {
        this.angle = angle;
        recalculateWorldVertices();
    }

    @Override
    protected Hit isTouchingCircle(CircleCollider collider) {
        // Can be skipped since we normally won't need to check if a polygon is colliding with a
        // circle, just if the ball is colliding with polygons.

        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected Hit isTouchingPolygon(PolygonCollider otherCollider) {
        PolygonCollider collider1 = this;
        PolygonCollider collider2 = otherCollider;

        double overlap = Double.POSITIVE_INFINITY;
        Vector2d overlapEdgeNormal = null;

        // We need to calculate separating lines for the normals of both objects
        for (int step = 0; step <= 1; step++) {
            if (step == 1) {
                collider1 = otherCollider;
                collider2 = this;
            }

            for (Vector2d n : collider1.normals) {
                double aMin = Double.POSITIVE_INFINITY;
                double aMax = Double.NEGATIVE_INFINITY;
                for (Vector2d v : collider1.worldVertices) {
                    double dotProduct = n.dotp(v);
                    aMin = Math.min(aMin, dotProduct);
                    aMax = Math.max(aMax, dotProduct);
                }

                double bMin = Double.POSITIVE_INFINITY;
                double bMax = Double.NEGATIVE_INFINITY;
                for (Vector2d v : collider2.worldVertices) {
                    double dotProduct = n.dotp(v);
                    bMin = Math.min(bMin, dotProduct);
                    bMax = Math.max(bMax, dotProduct);
                }

                // Calculate overlap along projected axis
                double newOverlap = Math.min(aMax, bMax) - Math.max(aMin, bMin);
                if (newOverlap < overlap) {
                    overlap = newOverlap;
                    overlapEdgeNormal = n;
                }

                if (!(bMax >= aMin && aMax >= bMin)) {
                    return null;
                }
            }
        }

        Vector2d delta = otherCollider.getWorldCenter().subtract(getWorldCenter()).unit();
        delta = new Vector2d(delta.x * overlap, delta.y * overlap);

        return new Hit(delta, overlapEdgeNormal);
    }

    @Override
    protected void draw(GraphicsWrapper g) {
        for (int i = 0; i < worldVertices.size(); i++) {
            Vector2d v = worldVertices.get(i);
            Vector2d v2 = worldVertices.get((i + 1) % worldVertices.size());
            g.drawLine((int) v.x, (int) v.y, (int) v2.x, (int) v2.y, Color.green);
        }
    }

    private void recalculateWorldVertices() {
        worldVertices.clear();
        for (Vector2d v : origVertices) {
            var finalV = v.rotate(angle).add(worldPos);
            worldVertices.add(finalV);
        }
    }
}
