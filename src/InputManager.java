import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashSet;

public class InputManager implements KeyEventDispatcher {
    private final HashSet<Integer> pressedKeys = new HashSet<>();

    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        int eventID = e.getID();
        if (eventID == KeyEvent.KEY_PRESSED) {
            pressedKeys.add(e.getKeyCode());
        } else if (eventID == KeyEvent.KEY_RELEASED) {
            pressedKeys.remove(e.getKeyCode());
        }

        return false;
    }

    public boolean isPressed(int keyCode) {
        return pressedKeys.contains(keyCode);
    }
}
