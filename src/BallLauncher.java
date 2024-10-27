import java.awt.*;
import java.awt.event.KeyEvent;

public class BallLauncher extends Entity {
    public static final double MIN_ANGLE = 0;
    public static final double MAX_ANGLE = Math.PI;
    // PI / 5s / 1000ms = x rad / ms
    private static final double ROTATION_SPEED = Math.PI / 2 / 1000;
    private static final double LAUNCH_SPEED = 700;
    final Vector2d center;
    double angle = Math.PI / 2;

    Image image;

    public BallLauncher(int screenWidth) {
        center = new Vector2d(screenWidth / 2.0, 20);
        this.image = AssetLoader.loadImage("assets/ball_launcher.png");
    }

    @Override
    public void draw(GraphicsWrapper g) {
        g.drawImage(image, (int) (center.x - 50), (int) (center.y - 50), 100, 100, angle);
    }

    @Override
    public void update(float deltaTime) {
        if (Game.instance.introShown) {
            return;
        }

        if (Game.instance.inputManager.isPressed(KeyEvent.VK_LEFT)) {
            angle += ROTATION_SPEED * deltaTime;
        }

        if (Game.instance.inputManager.isPressed(KeyEvent.VK_RIGHT)) {
            angle -= ROTATION_SPEED * deltaTime;
        }

        angle = Math.max(MIN_ANGLE, Math.min(angle, MAX_ANGLE));

        if (Game.instance.inputManager.isPressed(KeyEvent.VK_SPACE)) {
            // Get the ball entity
            Ball ball = (Ball) Game.instance.getEntities().stream()
                    .filter(x -> x instanceof Ball)
                    .findFirst().get();

            if (ball.getAlive()) {
                return;
            }

            // Calculate launch vector
            Vector2d launch = new Vector2d(1, 0).rotate(angle);
            Vector2d launchOrigin = center.add(launch.scalarMult(50));

            ball.setCenter(launchOrigin);
            ball.setVelocity(launch.scalarMult(LAUNCH_SPEED));
            ball.setAlive(true);
        }
    }
}
