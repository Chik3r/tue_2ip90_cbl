import java.awt.*;

public class CircleCollider extends Collider {
    private final float radius;
    private Vector2d oldPos;

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

    public void setOldPos(Vector2d oldPos) {
        this.oldPos = oldPos;
    }

    private Vector2d getOldWorldPos() {
        return center.add(oldPos);
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
        // cc = unit vector of bb, and lambda = distance - radius of both circles
        // Then the delta = P + lambda * cc
        double lambda = distanceVector.length() - sumRadius;
        Vector2d delta = distanceVector.unit().scalarMult(lambda);

        return new Hit(delta, distanceVector);
    }

    @Override
    protected Hit isTouchingPolygon(PolygonCollider collider) {
        int numVerticesPolygon = collider.worldVertices.size();

        double time = Double.POSITIVE_INFINITY;
        LineHit lineHitInfo = null;
        for (int i = 0; i < numVerticesPolygon; i++) {
            LineHit hit = isTouchingLineSweep(collider.worldVertices.get(i), collider.worldVertices.get((i + 1) % numVerticesPolygon));
            if (hit == null) {
                continue;
            }

            if (hit.time < time) {
                time = hit.time;
                lineHitInfo = hit;
            }
        }

        if (lineHitInfo == null) {
            return null;
        }

        return new Hit(lineHitInfo.delta, lineHitInfo.normal);
    }

    @Override
    protected void draw(GraphicsWrapper g) {
        Vector2d worldCenter = getWorldCenter();
        g.drawOval((int) (worldCenter.x - radius), (int) (worldCenter.y - radius), (int) (radius*2), (int) (radius*2), Color.green);
    }

    private LineHit isTouchingLineSweep(Vector2d lineStart, Vector2d lineEnd) {
        Vector2d oldWorldPos = getOldWorldPos();
        Vector2d worldCenter = getWorldCenter();
        var circleVel = worldCenter.subtract(oldWorldPos);
        circleVel = circleVel.add(circleVel.unit().scalarMult(radius)); // Extend velocity by radius
        var lineVel = lineEnd.subtract(lineStart);

        // Check if the path of the circle ever hits the line using fancy matrix math.
        // See https://stackoverflow.com/questions/73079419/intersection-of-two-vector
        double denominator = circleVel.x * -lineVel.y + lineVel.x * circleVel.y;
        if (denominator == 0) {
            // No collision
            return null;
        }

        Vector2d vecOrigins = lineStart.subtract(oldWorldPos);
        double timeCircle = (lineVel.x * vecOrigins.y - lineVel.y * vecOrigins.x) / denominator;
        double timeLine = (circleVel.x * vecOrigins.y - circleVel.y * vecOrigins.x) / denominator;

        if (timeCircle >= 0 && timeCircle <= 1 && timeLine >= 0 && timeLine <= 1) {
            // Circle hit the line at some point within the line segment
            // Calculate the circle's actual center by moving the point back by radius

            // 0.1 needed to prevent double precision errors
            var delta = calculateDelta(timeCircle, oldWorldPos, circleVel, worldCenter, -0.1);
            return new LineHit(delta, lineVel.normal(), timeCircle);
        }

        // The circle hit the line outside the line segment, test if it hit one of the vertices.
        double timeCircleLineStart = testPointHit(circleVel, lineStart);
        double timeCircleLineEnd = testPointHit(circleVel, lineEnd);
        double timeCircleLine = Math.min(timeCircleLineStart, timeCircleLineEnd);
        if (Double.isNaN(timeCircleLine) || timeCircleLine < 0 || timeCircleLine > 1) {
            // No solution
            return null;
        }

        // Calculate the delta
        var delta = calculateDelta(timeCircleLine, oldWorldPos, circleVel, worldCenter, 0);
        return new LineHit(delta, lineVel.normal(), timeCircleLine);
    }

    private double testPointHit(Vector2d vel, Vector2d point) {
        Vector2d difference = getOldWorldPos().subtract(point);

        // circlePath = oldWorldPos + t * delta
        // calculate t for distanceSquared(circlePath, point) = r * r
        // a, b, c are parts of a quadratic equation for t
        double a = vel.lengthSquared();
        double b = 2 * vel.dotp(difference);
        double c = difference.lengthSquared() - radius * radius;

        double discriminant = b * b - 4 * a * c;
        if (discriminant < 0) {
            // No solution
            return Double.NaN;
        }

        double t1 = (-b + Math.sqrt(discriminant)) / (2 * a);
        double t2 = (-b - Math.sqrt(discriminant)) / (2 * a);
        return Math.min(t1, t2);
    }

    private static Vector2d calculateDelta(double time, Vector2d start, Vector2d vel,
                                           Vector2d currentPos, double offsetScalar) {
        // Start + t * vel + (unit vel * offsetScalar)
        Vector2d truePos = start.add(vel.scalarMult(time)).add(vel.unit().scalarMult(offsetScalar));
        return truePos.subtract(currentPos);
    }

    private record LineHit(Vector2d delta, Vector2d normal, double time) { }
}
