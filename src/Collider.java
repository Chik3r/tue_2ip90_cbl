public abstract class Collider {
    protected Vector2d center;
    protected Vector2d worldPos;

    public Collider(Vector2d center) {
        this.center = center;
        this.worldPos = new Vector2d(0, 0);
    }

    public Collider(Vector2d center, Vector2d worldPos) {
        this.center = center;
        this.worldPos = worldPos;
    }

    public boolean isTouching(Collider collider) {
        if (collider instanceof CircleCollider) {
            return isTouchingCircle((CircleCollider) collider);
        } else if (collider instanceof PolygonCollider) {
            return isTouchingPolygon((PolygonCollider) collider);
        }

        System.out.println("Unknown collider type!");
        return false;
    }

    public void setWorldPos(Vector2d worldPos) {
        this.worldPos = worldPos;
    }

    protected Vector2d getWorldCenter() {
        return center.add(worldPos);
    }

    protected abstract boolean isTouchingCircle(CircleCollider collider);
    protected abstract boolean isTouchingPolygon(PolygonCollider collider);
}
