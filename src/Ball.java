import java.awt.*;
// import javax.swing.JFrame;  
// 
public class Ball extends Entity {
    private Vector2d pos; 
    private Vector2d velocity;
    static final int RADIUS = 25; // arbitrary number. needs to be tested
    static final double GRAVITY = 980; // arbitrary number. needs to be tested
    static final double STARTING_SPEED = 1500; //same here
    Toolkit t = Toolkit.getDefaultToolkit();
    Image kees = t.getImage("assets/kees_ball.png");
    
    public Ball(int x, int y) {
        this.pos = new Vector2d(x, y);
        this.velocity = new Vector2d(30, 1).unit().scalarmult(STARTING_SPEED);
    }

    public Vector2d getPos() {
        return pos;
    }

    public void draw(Graphics g, Rectangle bounds) {
        g.drawImage(kees, (int) pos.x, (int) pos.y, RADIUS, RADIUS, null);
        // System.out.println("I am being drawn");
    }

    public void update(float deltaTime) {
        updateVelocity(deltaTime);
        collisionCheck();
        updatePos(deltaTime);
    }

    public void updateVelocity(float deltaTime) {
        //deltatime in seconds
        velocity.y += GRAVITY * (deltaTime / 1000);
    }
    public void updatePos(float deltaTime) {
        //deltatime in seconds
        pos.x += velocity.x * (deltaTime / 1000);
        pos.y += velocity.y * (deltaTime / 1000);    
    }

    public void collisionCheck() {
        //check if the object collided with any entity or the side/top walls
        //if it did call the appropriate method
    }

    public void collisionCircle() {
        // calculate the tangent of the static cirle at the point of collision
        // get the normal unit vector of that tangent
        // dotp of the velocity and unit vector * (pointed invard)normal unit vector == parallel (to normal) component
        // vecolity - parallel component == parallel (to tangent) component
        // parallel (to tangent) - parallel (to normal) = reflected vector
        // reflected vector is the new velocity
    }

    public void collisionRect() {
        // this also includes the walls

        // if we only have rectangles that arent rotated then this is more than enough
        // otherwise we would need to get the line of the side it collides with and do the same as the circle collision

        // if the collision happened on the side then reverse x
        // if the collision happened on the top/bottom ten reverse y
    }
}