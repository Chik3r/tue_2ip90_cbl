import java.awt.event.KeyEvent;

public class BallLauncher extends Entity {
    // PI / 5s / 1000ms = x rad / ms
    private static final double ROTATION_SPEED = Math.PI / 3 / 1000;
    private static final double LAUNCH_SPEED = 1000;
    public static final double MIN_ANGLE = 0;
    public static final double MAX_ANGLE = Math.PI;
    Vector2d center;
    double angle = Math.PI / 2;

    public BallLauncher(int screenWidth) {
        center = new Vector2d(screenWidth / 2.0, 20);
    }

    @Override
    public void draw(GraphicsWrapper g) {
        // TODO: Draw launcher
    }

    @Override
    public void update(float deltaTime) {
        if (Game.instance.inputManager.isPressed(KeyEvent.VK_LEFT)) {
            angle += ROTATION_SPEED * deltaTime;
        }

        if (Game.instance.inputManager.isPressed(KeyEvent.VK_RIGHT)) {
            angle -= ROTATION_SPEED * deltaTime;
        }

        angle = Math.max(MIN_ANGLE, Math.min(angle, MAX_ANGLE));

        if (Game.instance.inputManager.isPressed(KeyEvent.VK_SPACE)) {
            System.out.println("Launching ball with angle: " + angle);

            // TODO: Only launch a ball if the ball is not active
            if (false) {
                return;
            }

            // Get the ball entity
            Ball ball = (Ball) Game.instance.getEntities().stream()
                    .filter(x -> x instanceof Ball)
                    .findFirst().get();

            // Calculate launch vector
            Vector2d launch = new Vector2d(1, 0).rotate(angle);
            Vector2d launchOrigin = center.add(launch.scalarMult(/*TODO: SOME CONSTANT FOR OFFSET*/ 1));

            ball.setPos(launchOrigin);
            ball.setVelocity(launch.scalarMult(LAUNCH_SPEED));
        }
    }
}
