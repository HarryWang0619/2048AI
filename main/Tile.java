import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Tile {

    public static final int WIDTH = 80;
    public static final int HEIGHT = 80;
    public static final int SLIDE_SPEED = 20;

    private int value;
    private BufferedImaga tileImage;
    private Color background;
    private Color text;
    private Font font;
    private int x;
    private int y;


    public Tile(int value, int x, int y) {
        this.value = value;
        this.x = x;
        this.y = y;
        tileImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        drawImage();
    }

    private void drawImage() {
        Graphics2D g = (Graphics2D) tileImage.getGraphics();
        if (value == 2) {
            background = new Color(0xfff4d3);
            text = new Color(0x474747);
        } else if (value == 4) {
            background = new Color(0xffdac3);
            text = new Color(0x474747);
        } else if (value == 8) {
            background = new Color(0xe7b08e);
            text = new Color(0xe3e3e3);
        } else if (value == 16) {
            background = new Color(0xe7bf8e);
            text = new Color(0xe3e3e3);
        } else if (value == 32) {
            background = new Color(0xffc4c3);
            text = new Color(0xe3e3e3);
        } else if (value == 64) {
            background = new Color(0xE7948e);
            text = new Color(0xe3e3e3);
        } else if (value == 128) {
            background = new Color(0xbe7e56);
            text = new Color(0xe3e3e3);
        } else if (value == 256) {
            background = new Color(0xbe5e56);
            text = new Color(0xe3e3e3);
        } else if (value == 512) {
            background = new Color(0x9c3931);
            text = new Color(0xe3e3e3);
        } else if (value == 1024) {
            background = new Color(0x701710);
            text = new Color(0xe3e3e3);
        } else if (value == 2048) {
            background = new Color(0x701710);
            text = new Color(0x107036);
        } else {
            background = new Color(0x107036);
            text = new Color(0x701710);
        }  

        g.setColor(new Color(0,0,0,0));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.setColor(text);

        if (value <= 64) {
            font = Game.main.deriveFont(36f);
        } else {
            font = Game.main;
        }
        
        g.setFont(font);

        int drawX = WIDTH/2 - DrawUtils.getMessageWidth("" + value, font, g) / 2;
        int drawY = HEIGHT/2 + DrawUtils.getMessageHeight("" + value, font, g) / 2;
        g.drawString("" + value, drawX, drawY);
        g.dispose();
    }
}