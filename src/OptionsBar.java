import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;

public class OptionsBar {
    public Entity lastEntity;
    private long lastUpdateTime;
    private static final long TIME_BETWEEN_PLACING = 1_000_000_000 / 2;
    private static final long TIME_BETWEEN_CHANGES = 1_000_000_000 / 20;
    private String baseText;
    private String displayText;

    public Vector2d pos;

    public OptionsBar(Rectangle frameBounds) {
        pos = new Vector2d(frameBounds.getWidth()/2, frameBounds.y);
        lastUpdateTime = System.nanoTime();
        baseText = "R - Rectangle Brush\nC - Circle Brush\nE - Eraser\nS - Save\nESC - No Brush";
        displayText = baseText;
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

    public void clearBrush() {
        LevelEditor.instance.removeEntity(lastEntity);
        lastEntity = null;
        LevelEditor.instance.placing = false;
    }

    public void saveLevel() {
        LevelEditor.instance.saveLevel();
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

    private String changeText(Entity entity) {
        String str = 
        "\nPos:    " + ((Peg) entity).pos.x + " " + ((Peg) entity).pos.y + 
        "\nWidth:  " + (entity instanceof CirclePeg ? ((CirclePeg) entity).radius : ((RectPeg) entity).width) +
        "\nHeight: " + (entity instanceof CirclePeg ? ((CirclePeg) entity).radius : ((RectPeg) entity).height) +
        "\nAngle:  " + ((entity instanceof CirclePeg ? "" : Math.toDegrees(((RectPeg) entity).angle))) +
        "\nOrange: " + ((Peg) entity).orange;

        return baseText + str;
    }

    public void draw(GraphicsWrapper g, Font font) {
        g.setColor(Color.BLACK);
        g.drawString(displayText, (int) pos.x, (int) pos.y, font);
    }

    /**
     * CONTROLS:
     * 
     * NUM 7 | 9 - LEFT | RIGHT ROTATE RECTPEGS
     * NUM 4 | 6 - GROW | SHRINK HEIHGT/RADIUS
     * NUM 1 | 3 - GROW | SHRINK WIDTH/RADIUS
     * NUM 5 - ORANGE TOGGLE
     * 
     * R - RECTANGLE BRUSH
     * C - CIRCLE BRUSH
     * E - ERASER
     * 
     * ARROW KEYS - PERCISION MOVEMENT
     * ((oops frogor to implement))
     * 
     * ENTER = PLACE
     */
    public void update(float deltaTime, Boolean placing) {
        var manager = LevelEditor.instance.inputManager;
        long now = System.nanoTime();
        if (manager.isPressed(KeyEvent.VK_R)) {
            createRect(false);
        }
        if (manager.isPressed(KeyEvent.VK_C)) {
            createCirc(false);
        }
        if (manager.isPressed(KeyEvent.VK_E)) {
            createEraser(false);
        }
        if (placing) {
            mouseSnap();
            if (now - lastUpdateTime > TIME_BETWEEN_CHANGES)
                if (lastEntity instanceof CirclePeg) {
                    var circEntity = ((CirclePeg) lastEntity);
                    // grow and shrink radius
                    if (manager.isPressed(KeyEvent.VK_NUMPAD6) || manager.isPressed(KeyEvent.VK_NUMPAD3)) {
                        circEntity.radius += 1;
                        lastUpdateTime = System.nanoTime();
                    }
                    if (manager.isPressed(KeyEvent.VK_NUMPAD1) || manager.isPressed(KeyEvent.VK_NUMPAD4)) {
                        circEntity.radius -= 1;
                        lastUpdateTime = System.nanoTime();
                    }
                    // swap orange
                    if (manager.isPressed(KeyEvent.VK_NUMPAD5)) {
                        circEntity.orange = !circEntity.orange;
                        lastUpdateTime = System.nanoTime();
                    }
                    //place 
                    if (now - lastUpdateTime > TIME_BETWEEN_PLACING && manager.isPressed(KeyEvent.VK_ENTER) && !(circEntity instanceof Eraser)) {
                        createCirc(true);
                        lastUpdateTime = System.nanoTime();
                    }
                    circEntity.updateCollider();
                } else if (lastEntity instanceof RectPeg) {
                    var rectEntity = ((RectPeg) lastEntity);
                    // grow and shrink angle
                    if (manager.isPressed(KeyEvent.VK_NUMPAD7)) {
                        rectEntity.angle += Math.toRadians(1);
                        lastUpdateTime = System.nanoTime();
                    }
                    if (manager.isPressed(KeyEvent.VK_NUMPAD9)) {
                        rectEntity.angle -= Math.toRadians(1);
                        lastUpdateTime = System.nanoTime();
                    }
                    // grow and shrink width
                    if (manager.isPressed(KeyEvent.VK_NUMPAD4)) {
                        rectEntity.width -= 1;
                        lastUpdateTime = System.nanoTime();
                    }
                    if (manager.isPressed(KeyEvent.VK_NUMPAD6)) {
                        rectEntity.width += 1;
                        lastUpdateTime = System.nanoTime();
                    }
                    // grow and shrink height
                    if (manager.isPressed(KeyEvent.VK_NUMPAD1)) {
                        rectEntity.height -= 1;
                        lastUpdateTime = System.nanoTime();
                    }
                    if (manager.isPressed(KeyEvent.VK_NUMPAD3)) {
                        rectEntity.height += 1;
                        lastUpdateTime = System.nanoTime();
                    }
                    // swap orange
                    if (manager.isPressed(KeyEvent.VK_NUMPAD5)) {
                        rectEntity.orange = !rectEntity.orange;
                        lastUpdateTime = System.nanoTime();
                    }
                    //place 
                    if (now - lastUpdateTime > TIME_BETWEEN_PLACING && manager.isPressed(KeyEvent.VK_ENTER)) {
                        createRect(true);
                        lastUpdateTime = System.nanoTime();
                    }
                    rectEntity.updateCollider();
                }
            if (lastEntity instanceof Eraser) {
                if (manager.isPressed(KeyEvent.VK_ENTER)) {
                    erase();
                }
            }
            displayText = changeText(lastEntity);
        }
        if (manager.isPressed(KeyEvent.VK_S)) {
            clearBrush();
            saveLevel();
        }
        if (manager.isPressed(KeyEvent.VK_ESCAPE)) {
            clearBrush();
        }
    }
}
