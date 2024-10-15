public abstract class Collider {
    public boolean isTouching(Collider collider) {
        return true;
    }

    protected abstract boolean isTouchingCircle(CircleCollider collider);
    protected abstract boolean isTouchingPolygon(PolygonCollider collider);
}
