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
    protected Hit isTouchingCircle(CircleCollider collider) {
        var selfWorldCenter = getWorldCenter();
        var otherWorldCenter = collider.getWorldCenter();
        var distanceVector = otherWorldCenter.subtract(selfWorldCenter);
        var sumRadius = radius + collider.radius;

        if (distanceVector.lengthSquared() > sumRadius * sumRadius) {
            return null;
        }

        // Let P = world center of this collider, bb = vector between the centers of the circles
        // cc = unit vector of bb, and lambda = distance - radius of this circle
        // Then the delta = P + lambda * cc
        double lambda = distanceVector.length() - radius;
        Vector2d delta = selfWorldCenter.add(distanceVector.unit().scalarMult(lambda));

        return new Hit(delta, distanceVector.normal());
    }

    @Override
    protected Hit isTouchingPolygon(PolygonCollider collider) {
        return null;
    }
}
