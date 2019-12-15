import java.util.Random;
import java.util.Scanner;
import javax.swing.*;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.awt.Color;
import java.awt.Dimension;

import java.awt.image.BufferedImage;

import javax.swing.JPanel;
 
public class Game extends JPanel implements KeyListener, Runnable{

	private static final long serialVersionUID = 1L;

	// public static int score = 0;
	// public static Random random = new Random();
	
	public static final int WIDTH = 400;
	public static final int HEIGHT = 600; 
	public static final Font main = new Font("Futura", Font.PLAIN, 28);
	private Thread game;
	private boolean running;
	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private GameBoard board;

	private long startTime; 
	private long elapsed;
	private boolean set;

	public Game() {
		setFocusable(true);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		addKeyListener(this);

		board = new GameBoard(WIDTH/2 - GameBoard.BOARD_WIDTH/2, HEIGHT - GameBoard.BOARD_HEIGHT - 10);
	}

	public void update() {
		board.update();
		KeyboardAgent.update();
	}

	private void render() {
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setColor(new Color(0xFAF8EF));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		board.render(g);
		g.dispose();

		Graphics2D g2d = (Graphics2D) getGraphics();
		g2d.drawImage(image, 0, 0, null);
		g2d.dispose();
	}

	public void run() {
		int fps = 0;
		int updates = 0;
		long fpsTimer = System.currentTimeMillis();
		double nsPerUpdate = 1000000000.0/60; 

		double then = System.nanoTime();
		double unprocessed = 0;
		
		while(running) {

			double now = System.nanoTime();
			boolean shouldRender = false;
			unprocessed += (now - then) / nsPerUpdate;
			then = now;

			while(unprocessed >= 1) {
				updates++;
				update();
				unprocessed--;
				shouldRender = true;
			}
	
			if(shouldRender) {
				fps++;
				render();
				shouldRender = false;
			} else {
				try {
					Thread.sleep(1);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
	
			if(System.currentTimeMillis() - fpsTimer > 1000) {
				System.out.printf("%d fps %d updates", fps, updates);
				System.out.println();
				fps = 0;
				updates = 0;
				fpsTimer += 1000;
			}
		}
	}
	
	public synchronized void start() {
		if(running) return;
		running = true;
		game = new Thread(this, "game");
		game.start();
	}

	public synchronized void stop() {
		if (!running) return;
		running = false;
		System.exit(0);
	}

	public void keyPressed(KeyEvent e) {
		KeyboardAgent.KeyPressed(e);
	}

	public void keyReleased(KeyEvent e) {
		KeyboardAgent.keyReleased(e);
	}

	public void keyTyped(KeyEvent e) {
	}

}
