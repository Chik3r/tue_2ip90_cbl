/**
 * Peg.
 */
public abstract class Peg extends Entity {
    protected Vector2d pos;
    protected boolean orange;
    protected boolean beenHit;
    //protected collider obj

    public void getHit() {
        beenHit = true;
    }

    public void getCleared() {
        if (beenHit) {
            // implement propper getting cleared
            // set this to null and remove from entities in game
        }
    }

}
