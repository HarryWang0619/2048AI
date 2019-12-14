import java.awt.Font;
import java.awt.Graphics2D;
import java.java.awt.font.TextLayout;

public class DrawUtils{
    private DrawUtils() {}

    public static int getMessageWidth(String message, Font font, Graphics2D g) {
        g.setFont(font);
        Rectangle2D bounds = g.getFontMetrics().getStringBounds(message, g);
        return (int) bounds.getWidth();
    }

    public static int getMessageHeight(String message, Font font, Graphics2D g) {
        g.setFont(font);
        if (message.length() ==0) {
            return 0;
        }
        java.awt.font.TextLayout tl = new TextLayout(message, font, g.getFontRenderContext());
        return (int) tl.getBounds().getHeight();
    }
}