import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;

public class OptionsBar {
    public JPanel options;
    public Entity lastEntity;
    public ActionListener al;

    private static final double ROTATION_SPEED = Math.PI / 2 / 1000;
    public static final double MIN_ANGLE = 0;
    public static final double MAX_ANGLE = Math.PI;

    public OptionsBar(JFrame frame) {
        options = new JPanel();
        options.setBounds(0, 0, 100, 200);
        options.setOpaque(false);

        JButton r = new JButton("rectangle");
        r.addActionListener(e -> createRect(false));
        JButton c = new JButton("circle");
        c.addActionListener(e -> createCirc(false));
        JButton eraser = new JButton("eraser");
        eraser.addActionListener(e -> createEraser(false));
        JButton save = new JButton("save");
        save.addActionListener(e -> saveLevel());

        JLabel pos = new JLabel("pos: ");
        JLabel radius = new JLabel("radius: ");
        JLabel width = new JLabel("width: ");
        JLabel height = new JLabel("height: ");
        JLabel angle = new JLabel("angle: ");
        JLabel orange = new JLabel("orange: ");

        options.add(r);
        options.add(c);
        options.add(eraser);
        options.add(pos);
        options.add(radius);
        options.add(width);
        options.add(height);
        options.add(angle);
        options.add(orange);
        options.add(save);
    }

    public void createRect(Boolean placed) {
        if (!placed) {
            LevelEditor.instance.removeEntity(lastEntity);
        }
        var rect = new RectPeg(0, 0, 30, 20, false);
        lastEntity = rect;
        LevelEditor.instance.addEntity(rect);
        LevelEditor.instance.placing = true;
    }

    public void createCirc(Boolean placed) {
        if (!placed) {
            LevelEditor.instance.removeEntity(lastEntity);
        }
        var circ = new CirclePeg(0, 0, 10, false);
        lastEntity = circ;
        LevelEditor.instance.addEntity(circ);
        LevelEditor.instance.placing = true;
    }

    public void createEraser(Boolean placed) {
        if (!placed) {
            LevelEditor.instance.removeEntity(lastEntity);
        }
        var eras = new Eraser(0, 0);
        lastEntity = eras;
        LevelEditor.instance.addEntity(eras);
        LevelEditor.instance.placing = true;
    }

    public void saveLevel() {
        LevelEditor.instance.saveLevel();
    }

    public void draw(Graphics2D g) {
        // options.repaint(0);
    }

    private void mouseSnap() {
        Point2D mousePos = MouseInfo.getPointerInfo().getLocation();
        if (lastEntity instanceof Peg) {
            ((Peg) lastEntity).pos = new Vector2d((int) mousePos.getX(), (int) mousePos.getY());
        }
    }

    /**
     * NUM 7 = RECT
     * NUM 8 = CIRC
     * NUM 9 = ERASER
     * NUM 6 = SWAP ORANGE
     * NUM 5 = GROW WIDTH / RAD
     * NUM 4 = SHRINK WIDTH / RAD
     * NUM 2 = GROW HEIGHT
     * NUM 1 = SHRINK HEIGHT
     * 
     * 
     * LEFT = ROTATE LEFT
     * RIGHT = ROTATE RIGHT
     * 
     * ENTER = PLACE
     */
    public void update(float deltaTime, Boolean placing) {
        if (LevelEditor.instance.inputManager.isPressed(KeyEvent.VK_NUMPAD7)) {
            createRect(false);
        }
        if (LevelEditor.instance.inputManager.isPressed(KeyEvent.VK_NUMPAD8)) {
            createCirc(false);
        }
        if (LevelEditor.instance.inputManager.isPressed(KeyEvent.VK_NUMPAD9)) {
            createEraser(false);
        }
        
        if (placing) {
            mouseSnap();
            if (lastEntity instanceof RectPeg) {
                var rectEntity = ((RectPeg) lastEntity);
                if (LevelEditor.instance.inputManager.isPressed(KeyEvent.VK_LEFT)) {
                    rectEntity.angle += ROTATION_SPEED * deltaTime;
                }
                if (LevelEditor.instance.inputManager.isPressed(KeyEvent.VK_RIGHT)) {
                    rectEntity.angle -= ROTATION_SPEED * deltaTime;
                }
                if (LevelEditor.instance.inputManager.isPressed(KeyEvent.VK_ENTER)) {
                    createRect(true);
                }
                // rectEntity.angle = Math.max(MIN_ANGLE, Math.min(rectEntity.angle, MAX_ANGLE));
            }
        }
    }
}
