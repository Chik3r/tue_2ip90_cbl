public class CircleCollider extends Collider {
    private final float radius;

    public CircleCollider(float radius) {
        super(new Vector2d(0, 0));
        this.radius = radius;
    }

    public CircleCollider(Vector2d center, float radius) {
        super(center);
        this.radius = radius;
    }

    public CircleCollider(Vector2d center, Vector2d worldPos, float radius) {
        super(center, worldPos);
        this.radius = radius;
    }

    @Override
    protected boolean isTouchingCircle(CircleCollider collider) {
        var selfWorldCenter = getWorldCenter();
        var otherWorldCenter = collider.getWorldCenter();
        var distanceSquared = selfWorldCenter.distanceSquared(otherWorldCenter);
        var sumRadius = radius + collider.radius;

        return distanceSquared <= sumRadius * sumRadius;
    }

    @Override
    protected boolean isTouchingPolygon(PolygonCollider collider) {
        return false;
    }
}
