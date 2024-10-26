import java.awt.Color;

public class Eraser extends CirclePeg {

    public Eraser(int x, int y) {
        super(x, y, 10, false);
    }

    @Override
    public void draw(GraphicsWrapper g) {
        g.fillOval((int) pos.x, (int) pos.y, radius * 2, radius * 2, Color.GRAY);
        collider.draw(g);
    }




}
