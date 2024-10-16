import java.util.ArrayList;

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
        Vector2d selfWorldCenter = getWorldCenter();

        ArrayList<Vector2d> circleVertices = new ArrayList<>();
        ArrayList<Vector2d> polyVertices = collider.worldVertices;
        ArrayList<Vector2d> normals1 = collider.normals;
        ArrayList<Vector2d> normals2 = new ArrayList<>();

        for (int i = 0; i < polyVertices.size(); i++) {
            normals2.add(selfWorldCenter.subtract(polyVertices.get(i)));
        }

        double overlap = Double.POSITIVE_INFINITY;
        Vector2d overlapEdgeNormal = null;

        // We need to calculate separating lines for the normals of both objects
        for (int step = 0; step <= 1; step++) {
            if (step == 1) {
                normals1 = normals2;
                normals2 = collider.normals;
            }

            for (Vector2d n : normals1) {
                double aMin = Double.POSITIVE_INFINITY, aMax = Double.NEGATIVE_INFINITY;
                for (Vector2d v : polyVertices) {
                    double dotProduct = n.dotp(v);
                    aMin = Math.min(aMin, dotProduct);
                    aMax = Math.max(aMax, dotProduct);
                }

                // Extend from the center of the circle to the edge parallel to the normal,
                // and find the two points that will become the "vertices".
                circleVertices.clear();
                circleVertices.add(selfWorldCenter.add(n.unit().scalarMult(radius)));
                circleVertices.add(selfWorldCenter.subtract(n.unit().scalarMult(radius)));

                double bMin = Double.POSITIVE_INFINITY, bMax = Double.NEGATIVE_INFINITY;
                for (Vector2d v : circleVertices) {
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

        Vector2d delta = collider.getWorldCenter().subtract(getWorldCenter()).unit();
        delta = new Vector2d(delta.x * overlap, delta.y * overlap);

        return new Hit(delta, overlapEdgeNormal);
    }

    private Hit isTouchingLine(Vector2d lineStart, Vector2d lineEnd) {
        Vector2d worldCenter = getWorldCenter();

        var lineCircle = lineStart.subtract(worldCenter);
        var line = lineEnd.subtract(lineStart);
        var dot = lineCircle.dotp(line) / line.length();

        Vector2d closest = lineStart.add(line.scalarMult(dot));
        double closestDistanceSum = Math.sqrt(closest.distanceSquared(lineStart)) + Math.sqrt(closest.distanceSquared(lineEnd));
        if (Math.abs(closestDistanceSum - line.length()) > 0.001) {
            // 0.001 to account for some error
            // If the sum of the distance of the closest point to both extremes of the line is
            // greater than the length of the line, then the point isn't on the line
            return null;
        }

        // k should be the radius of the circle - the distance of the center to the closest point
        // Then delta is k * (line between closest point and circle)
        Vector2d centerClosestLine = worldCenter.subtract(closest);
        double k = radius - centerClosestLine.length();
        Vector2d delta = centerClosestLine.unit().scalarMult(k);

        return new Hit(delta, line.normal());
    }
}
