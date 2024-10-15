public abstract class Collider {
    protected Vector2d center;
    protected Vector2d worldPos;
    // TODO: Angle

    public Collider(Vector2d center) {
        this.center = center;
        this.worldPos = new Vector2d(0, 0);
    }

    public Collider(Vector2d center, Vector2d worldPos) {
        this.center = center;
        this.worldPos = worldPos;
    }

    public Hit isTouching(Collider collider) {
        if (collider instanceof CircleCollider) {
            return isTouchingCircle((CircleCollider) collider);
        } else if (collider instanceof PolygonCollider) {
            return isTouchingPolygon((PolygonCollider) collider);
        }

        System.out.println("Unknown collider type!");
        return null;
    }

    public void setWorldPos(Vector2d worldPos) {
        this.worldPos = worldPos;
    }

    protected Vector2d getWorldCenter() {
        return center.add(worldPos);
    }

    protected abstract Hit isTouchingCircle(CircleCollider collider);
    protected abstract Hit isTouchingPolygon(PolygonCollider collider);
    // TODO: Debug draw method
}
