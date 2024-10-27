import java.awt.*;

/**
 * Peg.
 */
public abstract class Peg extends Entity {
    protected Vector2d pos;
    protected boolean orange;
    protected boolean beenHit;
    protected Collider collider;

    public void gotHit() {
        beenHit = true;
    }

    public void clearing() {
        if (beenHit) {
            Game.instance.removeEntity(this);
        }
    }

    public Collider getCollider() {
        return collider;
    }

    protected Color determineOuterColor() {
        Color baseColorOuter = orange ? new Color(255, 153, 51) : Color.blue;

        if (!beenHit) {
            return baseColorOuter;
        }

        Color targetColorOuter = orange ? new Color(209, 107, 0) : new Color(0, 18, 181);
        float f = (float) (Math.sin(System.currentTimeMillis() / 1000.0 * Math.PI) + 1) / 2;
        return ColorUtil.colorLerp(baseColorOuter, targetColorOuter, f);
    }

    protected Color determineInnerColor() {
        Color baseColorInner = orange ? Color.orange : Color.cyan;

        if (!beenHit) {
            return baseColorInner;
        }

        Color targetColorInner = orange ? new Color(197, 135, 0) : new Color(0, 190, 207);
        float f = (float) (Math.sin(System.currentTimeMillis() / 1000.0 * Math.PI) + 1) / 2;
        return ColorUtil.colorLerp(baseColorInner, targetColorInner, f);
    }
}
