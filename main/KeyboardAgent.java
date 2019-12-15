import java.awt.event.KeyEvent;

public class KeyboardAgent {
    
    public static boolean[] pressed = new boolean[256];
    public static boolean[] prev = new boolean[64];

    private KeyboardAgent() { }

    public static void update() {
        for (int i = 0; i < 4; i++) {
            if (i == 0) {
                prev[KeyEvent.VK_LEFT] = pressed[KeyEvent.VK_LEFT];
            } else if (i == 1) {
                prev[KeyEvent.VK_RIGHT] = pressed[KeyEvent.VK_RIGHT];
            } else if (i == 2) {
                prev[KeyEvent.VK_UP] = pressed[KeyEvent.VK_UP];
            } else if (i == 3) {
                prev[KeyEvent.VK_DOWN] = pressed[KeyEvent.VK_DOWN];
            }
        }
    }

    public static void KeyPressed(KeyEvent e) {
        pressed[e.getKeyCode()] = true;
    }

    public static void keyReleased(KeyEvent e) {
        pressed[e.getKeyCode()] = false;
    }

    public static boolean typed(int keyEvent) {
        return !pressed[keyEvent] && prev[keyEvent];
    }
}