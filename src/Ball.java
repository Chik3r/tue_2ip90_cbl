import java.awt.*;

public class Ball extends Entity {
    private int x;
    private int y;
    static final int RADIUS = 25; // arbitrary number. needs to be tested
    static final float GRAVITY = 10; // arbitrary number. needs to be tested
    private float velocity = 25; //arbitrary number. needs to be tested
    
    public Ball(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics g, Rectangle bounds) {
        //TODO: graphics stuff
    }

    public void update(float deltaTime) {
        //TODO: physics stuff
    }
}
