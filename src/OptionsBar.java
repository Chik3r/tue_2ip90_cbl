import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;

public class OptionsBar {
    public JPanel options;
    public Entity lastEntity;
    private boolean entityPlaced;

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
        RectPeg rect;
        if (!placed) {
            LevelEditor.instance.removeEntity(lastEntity);
            rect = new RectPeg(0, 0, 30, 20, false);
        } else {
            rect = new RectPeg(0, 0, ((RectPeg) lastEntity).width, ((RectPeg) lastEntity).height, false, ((RectPeg) lastEntity).angle);
        }
        lastEntity = rect;
        LevelEditor.instance.addEntity(rect);
        LevelEditor.instance.placing = true;
    }

    public void createCirc(Boolean placed) {
        CirclePeg circ;
        if (!placed) {
            LevelEditor.instance.removeEntity(lastEntity);
            circ = new CirclePeg(0, 0, 10, false);
        } else {
            circ = new CirclePeg(0, 0, ((CirclePeg) lastEntity).radius, false);
        }
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

    public void erase() {
        for (Entity entity : LevelEditor.instance.getEntities()) {
            if (entity instanceof Peg && !entity.equals(lastEntity)) {
                Hit hit = ((Peg)lastEntity).getCollider().isTouching(((Peg) entity).getCollider());
                if (hit != null) {
                    ((Peg) entity).gotHit();
                }
            }
        }
    }

    private void mouseSnap() {
        Point2D mousePos = MouseInfo.getPointerInfo().getLocation();
        if (lastEntity instanceof Peg) {
            ((Peg) lastEntity).pos = new Vector2d((int) mousePos.getX(), (int) mousePos.getY());
            ((Peg) lastEntity).getCollider().setWorldPos(((Peg) lastEntity).pos);
        }
    }

    /**
     * NUM 4 | 6 - GROW | SHRINK HEIHGT/RADIUS
     * NUM 1 | 3 - GROW | SHRINK WIDTH/RADIUS
     * NUM 5 - ORANGE TOGGLE
     * 
     * R - RECTANGLE BRUSH
     * C - CIRCLE BRUSH
     * BACKSPACE - ERASER
     * 
     * ARROW KEYS - PERCISION MOVEMENT
     * 
     * ENTER - PLACE
     * 
     * LEFT = ROTATE LEFT
     * RIGHT = ROTATE RIGHT
     * 
     * ENTER = PLACE
     */
    public void update(float deltaTime, Boolean placing) {
        var manager = LevelEditor.instance.inputManager;
        if (manager.isPressed(KeyEvent.VK_R)) {
            createRect(false);
        }
        if (manager.isPressed(KeyEvent.VK_C)) {
            createCirc(false);
        }
        if (manager.isPressed(KeyEvent.VK_BACK_SPACE)) {
            createEraser(false);
        }
        
        if (placing) {
            mouseSnap();
            if (lastEntity instanceof CirclePeg) {
                var circEntity = ((CirclePeg) lastEntity);
                // grow and shrink radius
                if (manager.isPressed(KeyEvent.VK_NUMPAD6) || manager.isPressed(KeyEvent.VK_NUMPAD3)) {
                    circEntity.radius += 1;
                }
                if (manager.isPressed(KeyEvent.VK_NUMPAD1) || manager.isPressed(KeyEvent.VK_NUMPAD4)) {
                    circEntity.radius -= 1;
                }
                // swap orange
                if (manager.isPressed(KeyEvent.VK_NUMPAD5)) {
                    circEntity.orange = !circEntity.orange;
                }
                //place 
                if (manager.isPressed(KeyEvent.VK_ENTER) && !(circEntity instanceof Eraser)) {
                    createCirc(true);
                }
                circEntity.updateCollider();
            } else if (lastEntity instanceof RectPeg) {
                var rectEntity = ((RectPeg) lastEntity);
                // grow and shrink angle
                if (manager.isPressed(KeyEvent.VK_NUMPAD7)) {
                    // rectEntity.angle += ROTATION_SPEED * deltaTime;
                    rectEntity.angle += Math.toRadians(1);
                }
                if (manager.isPressed(KeyEvent.VK_NUMPAD9)) {
                    // rectEntity.angle -= ROTATION_SPEED * deltaTime;
                    rectEntity.angle -= Math.toRadians(1);
                }
                // grow and shrink width
                if (manager.isPressed(KeyEvent.VK_NUMPAD4)) {
                    rectEntity.width -= 1;
                }
                if (manager.isPressed(KeyEvent.VK_NUMPAD6)) {
                    rectEntity.width += 1;
                }
                // grow and shrink height
                if (manager.isPressed(KeyEvent.VK_NUMPAD1)) {
                    rectEntity.height -= 1;
                }
                if (manager.isPressed(KeyEvent.VK_NUMPAD3)) {
                    rectEntity.height += 1;
                }
                // swap orange
                if (manager.isPressed(KeyEvent.VK_NUMPAD5)) {
                    rectEntity.orange = !rectEntity.orange;
                }
                //place 
                if (manager.isPressed(KeyEvent.VK_ENTER)) {
                    createRect(true);
                }
                rectEntity.updateCollider();
            }
            if (lastEntity instanceof Eraser) {
                if (manager.isPressed(KeyEvent.VK_ENTER)) {
                    erase();
                }
            }
        }
    }
}
