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
}
