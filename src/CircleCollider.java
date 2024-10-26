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
        var lineVel = lineEnd.subtract(lineStart);

        // If the velocity/displacement of the circle is too small, swept collision won't work properly.
        // So we fall back to normal static collision
        if (circleVel.length() < 10) {
            Hit result = isTouchingLineSimple(lineStart, lineEnd);
            if (result != null) {
                return new LineHit(result.delta(), result.normal(), 0);
            }

            return null;
        }

        // Let p1 be the starting point of the line, p2 be the final point of the line
        // p3 be the old position of the circle, and p4 the new position of the circle.
        // Let r be the radius of the circle.
        // We can then take v1 = p4 - p3, and v2 = p2 - p1
        // Then the parametric equation for the line l = p1 + s*v2, and
        // the equation for the circle c = p3 + t*v1.
        // We want to find when the distance between these two lines is equal to the radius,
        // so we get: (l_x is the x component of l, similarly for other vectors)
        // (l_x - c_x)^2 + (l_y - c_y)^2 = r^2
        // If we replace l and c we get:
        // (p1_x + s*v2_x - (p3_x + t*v1_x))^2 + (p1_y + s*v2_y - (p3_y + t*v1_y))^2 = r^2
        // Let k = p1 - p3
        // We take the derivative of t with respect to s, and assume t'(s) = 0 in order to find
        // the values of t when dt/ds is 0.
        // This leaves us with the following equation:
        // t = ((k . v2) + s * (v2 . v2)) / (v1 . v2)
        // We then replace this t back into the original equation, getting a quadratic equation
        // which we can use to find s.
        // With the values we found for s, we try to calculate t, and we have found the earliest
        // and latest points in time where the circle collides with the line.
        // If no s or t can be found, then they didn't collide. And if t is not in [0, 1], then
        // the circle didn't hit the line during its motion.

        Vector2d vecOrigins = lineStart.subtract(oldWorldPos);
        double factorA = vecOrigins.dotp(lineVel);
        double factorB = lineVel.dotp(lineVel);
        double factorC = circleVel.dotp(lineVel);

        if (Math.abs(factorC) < 0.00000001) {
            // factorC is 0, equation can't be solved.
            // There probably is no hit, but make an extra check just in case
            Hit result = isTouchingLineSimple(lineStart, lineEnd);
            if (result != null) {
                return new LineHit(result.delta(), result.normal(), 0);
            }

            return null;
        }

        // parts of a quadratic equation for s
        double circleVelMagSquare = circleVel.dotp(circleVel);
        double kDotV1 = vecOrigins.dotp(circleVel);
        double quadEqA = factorB - (2 * factorC * factorB) / factorC
                + (circleVelMagSquare * factorB * factorB) / (factorC * factorC);
        double quadEqB = 2 * factorA - (2 * kDotV1 * factorB) / factorC
                - (2 * factorC * factorA) / factorC
                + (circleVelMagSquare * 2 * factorA * factorB) / (factorC * factorC);
        double kDotK = vecOrigins.dotp(vecOrigins);
        double quadEqC = kDotK - (2 * kDotV1 * factorA) / factorC
                + (factorA * factorA * circleVelMagSquare) / (factorC * factorC)
                - (radius * radius);

        double discriminant = quadEqB * quadEqB - 4 * quadEqA * quadEqC;
        if (discriminant < 0) {
            // Special check for outliers
            Hit result = isTouchingLineSimple(lineStart, lineEnd);
            if (result != null) {
                return new LineHit(result.delta(), result.normal(), 0);
            }

            // No solutions
            return null;
        }

        double sqrtDiscriminant = Math.sqrt(discriminant);
        double s1 = (-quadEqB + sqrtDiscriminant) / (2 * quadEqA);
        double s2 = (-quadEqB - sqrtDiscriminant) / (2 * quadEqA);

        if (s1 < 0 || s1 > 1) {
            s1 = Double.NaN;
        }
        if (s2 < 0 || s2 > 1) {
            s2 = Double.NaN;
        }

        // Now use s to find t
        double t1 = (factorA + s1 * factorB) / factorC;
        double t2 = (factorA + s2 * factorB) / factorC;
        double tToUse = Math.min(t1, t2);

        if (Double.isNaN(tToUse)) {
            // Special check for outliers
            Hit result = isTouchingLineSimple(lineStart, lineEnd);
            if (result != null) {
                return new LineHit(result.delta(), result.normal(), 0);
            }

            return null;
        }

        // Use the other time if the lowest one is too small
        if (tToUse < 0) {
            tToUse = tToUse == t1 ? t2 : t1;
        }
        if (tToUse < 0 || tToUse > 1) {
            return null;
        }

        Vector2d delta = calculateDelta(tToUse, oldWorldPos, circleVel, worldCenter, -0.1);

        return new LineHit(delta, lineVel.normal(), tToUse);
    }

    private Hit isTouchingLineSimple(Vector2d lineStart, Vector2d lineEnd) {
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

    private static Vector2d calculateDelta(double time, Vector2d start, Vector2d vel,
                                           Vector2d currentPos, double offsetScalar) {
        // Start + t * vel + (unit vel * offsetScalar)
        Vector2d truePos = start.add(vel.scalarMult(time)).add(vel.unit().scalarMult(offsetScalar));
        return truePos.subtract(currentPos);
    }

    private record LineHit(Vector2d delta, Vector2d normal, double time) { }
}
