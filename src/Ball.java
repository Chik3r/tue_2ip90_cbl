import java.awt.*;

/**
 * This is a Ball.
 */
public class Ball extends Entity {
    private Vector2d pos; 
    private Vector2d velocity;
    // TODO: test the perfect values for constants
    static final int RADIUS = 25; // radius of the ball
    static final double GRAVITY = 980; // the amount velocity.y should increase every second
    static final double STARTING_SPEED = 1500; //the length of velocity when shooting ball
    Toolkit t = Toolkit.getDefaultToolkit();
    Image kees = t.getImage("assets/kees_ball.png");
    
    /**
     * constructor for Ball.
     * @param x x coord of the top left of the ball
     * @param y y coord of the top left of the ball
     */
    public Ball(int x, int y) {
        this.pos = new Vector2d(x, y);
        // TODO: ball shooter should change this value to the vector
        // TODO: pointing from the center top of the screen to the end of the cannon
        this.velocity = new Vector2d(30, 1).unit().scalarmult(STARTING_SPEED);
    }

    public Vector2d getPos() {
        return pos;
    }

    /**
     * Draws the ball onto the canvas.
     * Currently draws Kees because fnuyy.
     */
    public void draw(Graphics g, Rectangle bounds) {
        g.drawImage(kees, (int) pos.x, (int) pos.y, RADIUS, RADIUS, null);
        // System.out.println("I am being drawn");
    }

    /**
     * Updates the velocity and position of the ball
     * then checks if it collides with anything.
     */
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
        Vector2d normalunit = new Vector2d(hit.normalX(), hit.normalY()).unit();
        // dotp of velocity and unit vector * normal unit vector == parallel (to normal) component
        double dotproduct = velocity.dotp(normalunit);
        Vector2d parallelcomponent = normalunit.scalarmult(dotproduct);
        // vecolity - parallel component == parallel (to tangent) component
        Vector2d perpendicularcomponent = velocity.subtract(parallelcomponent);
        // parallel (to tangent) - parallel (to normal) = reflected vector
        Vector2d reflectedvector = perpendicularcomponent.subtract(parallelcomponent);
        velocity = reflectedvector;

        // in the case of a wall/rect that has a side parallel to the x/y axis
        // this is the same as just reversing the x on side and y on top collision

        // of course this can be written better but this way its clearer for now
    }
}