import java.util.ArrayList;

public class PolygonCollider extends Collider {
    private ArrayList<Float> vertices;

    @Override
    protected boolean isTouchingCircle(CircleCollider collider) {
        return false;
    }

    @Override
    protected boolean isTouchingPolygon(PolygonCollider collider) {
        return false;
    }
}
