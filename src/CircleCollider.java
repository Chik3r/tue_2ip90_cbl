public class CircleCollider extends Collider {
    private float radius;
    private float offsetX;
    private float offsetY;

    @Override
    protected boolean isTouchingCircle(CircleCollider collider) {
        return false;
    }

    @Override
    protected boolean isTouchingPolygon(PolygonCollider collider) {
        return false;
    }
}
