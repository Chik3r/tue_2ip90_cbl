import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferStrategy;

import javax.imageio.ImageIO;
import java.io.*;
import java.util.*;

public class LevelEditor implements Runnable {
    private static final int FRAMES_PER_SECOND = 60;
    // Time between updates in nanoseconds
    private static final long TIME_BETWEEN_UPDATES = 1_000_000_000 / FRAMES_PER_SECOND;
    // Max number of times the physics can update between each render
    private static final int MAX_UPDATES_BETWEEN_RENDER = 1;

    public static LevelEditor instance;
    public boolean placing;

    private final ArrayList<Entity> entities = new ArrayList<>();
    JFrame frame;
    Rectangle frameBounds;
    BufferStrategy bufferStrategy;
    Thread gameLoopThread;


    Image background;

    public InputManager inputManager = new InputManager();
    public OptionsBar optionsBar;

    public LevelEditor() {
        instance = this;
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }

    @Override
    public void run() {

        initializeDrawing();
        initializeLevel("Danfy");
        optionsBar = new OptionsBar();

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

    private void initializeLevel(String level) {
        try {
            this.background = ImageIO.read(new File("levels/" + level + "/Backgr2.png"));
        } catch (IOException e) {
            //TODO: REMOVE THIS
            System.out.println("Nooooo you're pegging wrong 3 :((((");
        }
        File instructions = new File("levels\\Danfy\\Danfy.txt");
        Scanner scanner = null;
        try {
            scanner = new Scanner(instructions);
        } catch (FileNotFoundException e) {
            System.out.println("awawwwa");
        }

        initializeEntities(scanner);

        scanner.close();
    }

    private void initializeDrawing() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(1024, 768);
        frame.setVisible(true);

        Insets tmpInsets = frame.getInsets();
        frame.setSize(frame.getWidth() + tmpInsets.left + tmpInsets.right,
                frame.getHeight() + tmpInsets.top + tmpInsets.bottom);

        frame.createBufferStrategy(2);
        bufferStrategy = frame.getBufferStrategy();

        frameBounds = frame.getBounds();
        Insets insets = frame.getInsets();
        frameBounds.width -= insets.left + insets.right;
        frameBounds.x = insets.left;
        frameBounds.height -= insets.top + insets.bottom;
        frameBounds.y = insets.top;

        // frame.setSize(frame.getWidth() + 400, frame.getHeight());

        // frame.getContentPane().setLayout(null);
    }

    private void initializeEntities(Scanner scanner) {
        // entities.add(new BallLauncher(frameBounds.width));
        // entities.add(new Ball(0, 0));

        while (scanner.hasNextLine()) {
            String[] line = scanner.nextLine().split(" ");
            if (line[0].equals("c")) {
                int x = stringtoint(line[1]);
                int y = stringtoint(line[2]);
                int rad = stringtoint(line[3]);
                Boolean orange = Boolean.parseBoolean(line[4]);
                entities.add(new CirclePeg(x, y, rad, orange));
            } else if (line[0].equals("r")) {
                int a = stringtoint(line[1]);
                int b = stringtoint(line[2]);
                int c = stringtoint(line[3]);
                int d = stringtoint(line[4]);
                if (line.length == 6) {
                    Boolean orange = Boolean.parseBoolean(line[5]);
                    entities.add(new RectPeg(a, b, c, d, orange));
                } else if (line.length == 7) {
                    try {
                        int e = stringtoint(line[5]);
                        Boolean orange = line[6].equals("true");
                        entities.add(new RectPeg(a, b, c, d, e, orange));
                    } catch (NumberFormatException e) {
                        Boolean orange = line[5].equals("true");
                        double angle = Double.parseDouble(line[6]);
                        entities.add(new RectPeg(b, c, d, d, orange, angle));
                    }
                
                }
            }
        }

        // entities.add(new BorderCollider(new Rectangle(0, 0, frameBounds.width, frameBounds.height)));
    }

    private int stringtoint(String str) {
        String[] nums = str.split("\\+");
        int number = 0;
        for (String num : nums) {
            number += Integer.parseInt(num);
        }
        return number;
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
                g.fillRect(0, 0, frameBounds.width, frame.getHeight());

                // Draw Backgr
                wrapper.drawImage(background, 0, 0, frameBounds.width, frameBounds.height);

                // TODO: Depth
                for (Entity entity : entities) {
                    entity.draw(wrapper);
                }

                optionsBar.draw(g);

                g.dispose();
            } while (bufferStrategy.contentsRestored());
            
            bufferStrategy.show();
        } while (bufferStrategy.contentsLost());
        
    }

    public void clearEntities() {
        for (int i = entities.size() - 1; i >= 0; i--) {
            if (entities.get(i) instanceof Peg) {
                ((Peg) entities.get(i)).clearing();
            }
        }
    }
    
    private void update(float deltaTime) {
        optionsBar.update(deltaTime, placing);
        // for (Entity entity : entities) {
            // entity.update(deltaTime);
        // }
        clearEntities();
    }

    public void saveLevel() {
        try {
            FileWriter fWriter = new FileWriter("newLevel.txt");    
            
            for (Entity entity : entities) {
                if (entity instanceof CirclePeg) {
                    var pos = ((CirclePeg) entity).pos;
                    var radius = ((CirclePeg) entity).radius;
                    var orange = ((CirclePeg) entity).orange;
                    String line = pos.x + " " + pos.y + " " + radius + " " + orange;
                    fWriter.write(line + "\n");
                } else if (entity instanceof RectPeg) {
                    var pos = ((RectPeg) entity).pos;
                    var width = ((RectPeg) entity).width;
                    var height = ((RectPeg) entity).height;
                    var orange = ((RectPeg) entity).orange;
                    var angle = ((RectPeg) entity).angle;
                    String line = pos.x + " " + pos.y + " " + width + " " + height + " " + orange + " " + angle;
                    fWriter.write(line + "\n");
                }
            }

            fWriter.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}