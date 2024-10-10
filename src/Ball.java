import java.awt.*;

public class Ball extends Entity {
    private Vector2d pos; 
    static final int RADIUS = 25; // arbitrary number. needs to be tested
    static final double GRAVITY = 100; // arbitrary number. needs to be tested
    private Vector2d velocity;
    
    public Ball(int x, int y) {
        this.pos = new Vector2d(x, y);
        this.velocity = new Vector2d(0, 100);
    }

    public Vector2d getPos() {
        return pos;
    }

    public Vector2d newVelocity(Vector2d normal) {
        double dotproduct = velocity.dotp(normal);
        return new Vector2d(2*dotproduct*)
        //when bouncing at an angle calculate new velocity
    }

    public void draw(Graphics g, Rectangle bounds) {
        //TODO: graphics stuff
    }

    public void update(float deltaTime) {
        velocity.y += GRAVITY * deltaTime; // apply gravity
        
        pos.x += velocity.x; //change ball pos according to velocity
        pos.y += velocity.y;
        
        //check for collision

        //if collision happens
        //if it collides with a wall the angle simply reverse the x of the velocity
        //if it collides with a rectangle peg we need the surface nurmal unit vector
        //if it collides with a circle peg velocity 
    }

    public class Vector2d {
        public double x;
        public double y;
        
        public Vector2d(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double length() {
            return Math.sqrt(x * x + y * y);
        }

        public Vector2d normalunit() {
            return new Vector2d(-y/Math.sqrt(x*x+y*y), x/Math.sqrt(x*x+y*y));
        }

        public double dotp(Vector2d vector) {
            return x*vector.x + y*vector.y;
        }
    }
}