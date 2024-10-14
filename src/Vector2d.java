/**
 * 2 Dimensional vectors.
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

    public Vector2d unit() {
        return new Vector2d(x / length(), y / length());
    }

    public Vector2d normal() {
        return new Vector2d(-y, x);
    }

    public double dotp(Vector2d vector) {
        return x * vector.x + y * vector.y;
    }

    public Vector2d scalarmult(double scalar) {
        return new Vector2d(x * scalar, y * scalar);
    }
}