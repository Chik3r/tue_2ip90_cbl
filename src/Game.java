import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

public class Game implements Runnable {
    private final ArrayList<Entity> entities = new ArrayList<>();
    JFrame frame;
    Rectangle frameBounds;
    BufferStrategy bufferStrategy;
    Thread gameLoopThread;

    @Override
    public void run() {
        initialize();

        gameLoopThread = new Thread(this::gameLoop);
        gameLoopThread.start();
    }

    private void gameLoop() {
        while (true) {
            // Frame rate logic

            // Input logic

            update(0.01f);
            draw(0.01f);
        }
    }

    private void initialize() {
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

    private void draw(float deltaTime) {
        // Clear background
        do {
            do {
                Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
                // Clear image
                g.setColor(Color.white);
                g.fillRect(0, 0, frame.getWidth(), frame.getHeight());

                // TODO: Depth
                for (Entity entity : entities) {
                    entity.draw(deltaTime, g, frameBounds);
                }

                // Example drawing
                g.setColor(Color.gray);
                g.drawString("Hello world!", frameBounds.x, frameBounds.y + 20);
                double v = Math.sin(System.currentTimeMillis() / 1000.0) + 1;
                g.fillRect(frameBounds.x, frameBounds.y, (int) (v * 100), (int) (v * 100));

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
