import java.util.ArrayList;

public class Game implements Runnable {
    private final ArrayList<Entity> entities = new ArrayList<>();

    @Override
    public void run() {

    }

    private void Draw(float deltaTime) {
        for (Entity entity : entities) {
            entity.Draw(deltaTime);
        }
    }

    private void Update(float deltaTime) {
        for (Entity entity : entities) {
            entity.Update(deltaTime);
        }
    }
}
