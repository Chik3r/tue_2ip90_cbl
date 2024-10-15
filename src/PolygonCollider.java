import java.util.ArrayList;

public class PolygonCollider extends Collider {
    private ArrayList<Float> vertices;

    public PolygonCollider(Vector2d center) {
        super(center);
    }

    public PolygonCollider(Vector2d center, Vector2d worldPos) {
        super(center, worldPos);
    }

    @Override
    protected boolean isTouchingCircle(CircleCollider collider) {
        return false;
    }

    @Override
    protected boolean isTouchingPolygon(PolygonCollider collider) {
        return false;
    }
}
