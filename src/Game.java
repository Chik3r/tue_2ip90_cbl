import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

public class Game implements Runnable {
    private static final int FRAMES_PER_SECOND = 60;
    // Time between updates in nanoseconds
    private static final long TIME_BETWEEN_UPDATES = 1_000_000_000 / FRAMES_PER_SECOND;
    // Max number of times the physics can update between each render
    private static final int MAX_UPDATES_BETWEEN_RENDER = 1;

    public static Game instance;

    private final ArrayList<Entity> entities = new ArrayList<>();
    JFrame frame;
    Rectangle frameBounds;
    BufferStrategy bufferStrategy;
    Thread gameLoopThread;

    public InputManager inputManager = new InputManager();

    public Game() {
        instance = this;
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }

    @Override
    public void run() {
        initializeDrawing();
        initializeEntities();

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(inputManager);

        gameLoopThread = new Thread(this::gameLoop);
        gameLoopThread.start();
    }

    private void gameLoop() {
        long lastUpdateTime = System.nanoTime();

        long fpsStartTime = System.currentTimeMillis();
        int frameCount = 0;

        while (true) {
            if (frameCount % (FRAMES_PER_SECOND / 2) == 0) {
                System.out.println(frameCount / ((System.currentTimeMillis() - fpsStartTime) / 1000d));
            }

            // Input logic
            // ...

            // Frame rate
            long now = System.nanoTime();
            float elapsedMillis = (now - lastUpdateTime) / 1_000_000.0f;

            // Update the physics as many times as needed to catch up
            int updateCount = 0;
            while (now - lastUpdateTime >= TIME_BETWEEN_UPDATES && updateCount < MAX_UPDATES_BETWEEN_RENDER) {
                update(elapsedMillis);
                lastUpdateTime += TIME_BETWEEN_UPDATES;
                updateCount++;
            }

            // If we're still delayed, skip the remaining updates
            if (now - lastUpdateTime >= TIME_BETWEEN_UPDATES) {
                lastUpdateTime = now - TIME_BETWEEN_UPDATES;
            }

            draw(elapsedMillis);
            frameCount++;

            // Wait for enough time to have passed until the next frame
            long lastRenderTime = now;
            while (now - lastRenderTime < TIME_BETWEEN_UPDATES && now - lastUpdateTime <= TIME_BETWEEN_UPDATES) {
                Thread.yield();
                now = System.nanoTime();
            }
        }
    }

    private void initializeDrawing() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(1024, 768);
        frame.setVisible(true);

        frame.createBufferStrategy(2);
        bufferStrategy = frame.getBufferStrategy();

        frameBounds = frame.getBounds();
        Insets insets = frame.getInsets();
        frameBounds.width -= insets.left + insets.right;
        frameBounds.x = insets.left;
        frameBounds.height -= insets.top + insets.bottom;
        frameBounds.y = insets.top;
    }

    private void initializeEntities() {
        entities.add(new Ball(60, 50));
        entities.add(new CirclePeg(50, 450, 25, false));
        entities.add(new RectPeg(700, 50, 50, 500, false));
        entities.add(new RectPeg(50, 500, 700, 50, false));
        entities.add(new RectPeg(0, 50, 50, 500, false));
    }

    private void draw(float deltaTime) {
        // The official documentation recommends to have both of these while loops
        // https://docs.oracle.com/javase/6/docs/api/java/awt/image/BufferStrategy.html
        do {
            do {
                Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
                GraphicsWrapper wrapper = new GraphicsWrapper(g, frameBounds);
                // Clear frame
                g.setColor(Color.white);
                g.fillRect(0, 0, frame.getWidth(), frame.getHeight());

                // TODO: Depth
                for (Entity entity : entities) {
                    entity.draw(wrapper);
                }

                g.dispose();
            } while (bufferStrategy.contentsRestored());
            bufferStrategy.show();
        } while (bufferStrategy.contentsLost());
    }

    private void update(float deltaTime) {
        for (Entity entity : entities) {
            entity.update(deltaTime);
        }
    }
}
