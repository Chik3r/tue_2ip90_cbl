import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * This is a Ball.
 */
public class Ball extends Entity {
    // TODO: test the perfect values for constants
    static final int RADIUS = 25; // radius of the ball
    static final double GRAVITY = 980; // the amount velocity.y should increase every second
    static final double STARTING_SPEED = 1500; //the length of velocity when shooting ball

    private Vector2d pos; 
    private Vector2d velocity;

    Image kees;

    /**
     * Constructor for Ball.
     * @param x x coordinate of the top left of the ball
     * @param y y coordinate of the top left of the ball
     */
    public Ball(int x, int y) throws IOException {
        this.pos = new Vector2d(x, y);
        // TODO: ball shooter should change this value to the vector
        // TODO: pointing from the center top of the screen to the end of the cannon
        this.velocity = new Vector2d(30, 1).unit().scalarMult(STARTING_SPEED);

        this.kees = ImageIO.read(new File("assets/kees_ball.png"));
    }

    public Vector2d getPos() {
        return pos;
    }

    /**
     * Draws the ball onto the canvas.
     * Currently draws Kees because fnuyy.
     */
    @Override
    public void draw(GraphicsWrapper g) {
        g.drawImage(kees, (int) pos.x, (int) pos.y, RADIUS, RADIUS);
    }

    /**
     * Updates the velocity and position of the ball
     * then checks if it collides with anything.
     */
    @Override
    public void update(float deltaTime) {
        updateVelocity(deltaTime);
        updatePos(deltaTime);
        collisionCheck();
    }

    /**
     * Adds an amount of gravity to velocity based on deltaTime.
     * @param deltaTime the amount of time since the last update (in miliseconds)
     */
    public void updateVelocity(float deltaTime) {
        //deltatime in seconds
        velocity.y += GRAVITY * (deltaTime / 1000);
    }

    /**
     * Adds an amount of current velocity to position based on deltaTime.
     * @param deltaTime the amount of time since the last update (in miliseconds)
     */
    public void updatePos(float deltaTime) {
        //deltatime in seconds
        pos.x += velocity.x * (deltaTime / 1000);
        pos.y += velocity.y * (deltaTime / 1000);    
    }

    /**
     * Checks if the ball collided with anything after moving.
     */
    public void collisionCheck() {
        // TODO: implement collision check
        //check if the object collided with any entity or the side/top walls
        //if it did call create a Hit obj and call collisionCalc
    }

    /**
     * Upon collision resets the ball to before the collision and updates its velocity. 
     * @param hit The characteristics of the collision.
     */
    public void collisionCalc(Hit hit) {
        // puts ball back to before collision
        pos.x -= hit.deltaX();
        pos.y -= hit.deltaY();

        // calculate the tangent of the static cirle at the point of collision (Not needed with Hit)
        // (invard pointing) normal unit vector of the tangent
        Vector2d normalUnit = new Vector2d(hit.normalX(), hit.normalY()).unit();
        // dotp of velocity and unit vector * normal unit vector == parallel (to normal) component
        double dotProduct = velocity.dotp(normalUnit);
        Vector2d parallelComponent = normalUnit.scalarMult(dotProduct);
        // vecolity - parallel component == parallel (to tangent) component
        Vector2d perpendicularComponent = velocity.subtract(parallelComponent);
        // parallel (to tangent) - parallel (to normal) = reflected vector
        Vector2d reflectedVector = perpendicularComponent.subtract(parallelComponent);
        velocity = reflectedVector;

        // in the case of a wall/rect that has a side parallel to the x/y axis
        // this is the same as just reversing the x on side and y on top collision

        // of course this can be written better but this way its clearer for now
    }
}