/**
 * 2 Dimensional vectors.
 * 
 * For some god forsaken reason checkstyle keeps telling me that the class name
 * and file do not match WHEN IN FACT THEY DO!!!
 */
public class Vector2d {
    public double x;
    public double y;
    
    /**
     * constructor.
     * @param x x coord of vector
     * @param y y coord of vector
     */
    public Vector2d(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    public double lengthSquared() {
        return x * x + y * y;
    }

    public Vector2d unit() {
        return new Vector2d(x / length(), y / length());
    }

    public Vector2d normal() {
        return new Vector2d(-y, x);
    }

    public Vector2d add(Vector2d vector) {
        return new Vector2d(x + vector.x, y + vector.y);
    }

    /**
     * Subtracts the argument vector from the vector.
     * 
     * @param vector the vector to be subtracted
     * @return a new vector that points TO the original vector FROM the argument vector
     */
    public Vector2d subtract(Vector2d vector) {
        return new Vector2d(x - vector.x, y - vector.y);
    } 

    public double dotp(Vector2d vector) {
        return x * vector.x + y * vector.y;
    }

    public Vector2d scalarMult(double scalar) {
        return new Vector2d(x * scalar, y * scalar);
    }

    public double distanceSquared(Vector2d vector) {
        return (x - vector.x) * (x - vector.x) + (y - vector.y) * (y - vector.y);
    }

    public Vector2d rotate(double radians) {
        return new Vector2d(x * Math.cos(radians) - y * Math.sin(radians), x * Math.sin(radians) + y * Math.cos(radians));
    }

    public double angle(Vector2d vector) {
        int sign = y - vector.y < 0 ? -1 : 1;
        return sign * Math.acos(dotp(vector) / (length() * vector.length()));
    }

    public double angle() {
        return angle(new Vector2d(1, 0));
    }

    @Override
    public String toString() {
        return "Vector2d{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}