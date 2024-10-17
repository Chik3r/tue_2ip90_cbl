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
        int numVerticesPolygon = collider.worldVertices.size();

        double overlap = Double.NEGATIVE_INFINITY;
        Hit hitInfo = null;

        for (int i = 0; i < numVerticesPolygon; i++) {
            Hit hit = isTouchingLine(collider.worldVertices.get(i), collider.worldVertices.get((i + 1) % numVerticesPolygon));
            if (hit == null) {
                continue;
            }

            if (overlap < hit.delta().length()) {
                overlap = hit.delta().length();
                hitInfo = hit;
            }
        }

        return hitInfo;
    }

    private Hit isTouchingLine(Vector2d lineStart, Vector2d lineEnd) {
        Vector2d worldCenter = getWorldCenter();

        var centerToLineStart = worldCenter.subtract(lineStart);
        var line = lineEnd.subtract(lineStart);
        var dot = centerToLineStart.dotp(line) / line.length();

        Vector2d closest = lineStart.add(line.unit().scalarMult(dot));
        double closestDistanceSum = Math.sqrt(closest.distanceSquared(lineStart)) + Math.sqrt(closest.distanceSquared(lineEnd));
        if (Math.abs(closestDistanceSum - line.length()) > 0.001) {
            // 0.001 to account for some error
            // If the sum of the distance of the closest point to both extremes of the line is
            // greater than the length of the line, then the point isn't on the line

            // If the circle is near one of the vertices, then we need to calculate
            // the delta away from that vertex
            if (worldCenter.distanceSquared(lineStart) <= radius * radius) {
                // Similar calculations to the code at the end of the method, but calculating
                // against an edge instead of the closest point.
                double k = radius - centerToLineStart.length();
                Vector2d delta = centerToLineStart.unit().scalarMult(k);
                return new Hit(delta, line.normal());
            } else if (worldCenter.distanceSquared(lineEnd) <= radius * radius) {
                Vector2d centerEndLine = worldCenter.subtract(lineEnd);
                double k = radius - centerEndLine.length();
                Vector2d delta = centerEndLine.unit().scalarMult(k);
                return new Hit(delta, line.normal());
            }

            // The circle center isn't near the line segment or a vertex of the line, so
            // it isn't hitting the line.
            return null;
        }

        // k should be the radius of the circle - the distance of the center to the closest point
        // Then delta is k * (line between closest point and circle)
        Vector2d centerClosestLine = worldCenter.subtract(closest);

        double length = centerClosestLine.length();
        if (length > radius) {
            return null;
        }

        double k = radius - length;
        Vector2d delta = centerClosestLine.unit().scalarMult(k);

        return new Hit(delta, line.normal());
    }
}
