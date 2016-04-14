package in.vvest.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import in.vvest.gamestates.MainMenuState;

public class Game extends JPanel implements Runnable {
	private static final long serialVersionUID = 30L;
	public static final Font LARGE_FONT = new Font("consolas", Font.PLAIN, 50), MEDIUM_FONT = new Font("consolas", Font.PLAIN, 30), SMALL_FONT = new Font("consolas", Font.PLAIN, 12), XTRA_SMALL_FONT = new Font("consolas", Font.PLAIN, 8);
	public static final Color[] COLORS = {Color.WHITE, Color.RED, Color.BLUE, Color.ORANGE, new Color(255, 192, 203), Color.YELLOW, new Color(123, 63, 0)};
	public static final String[] COLOR_NAMES = {"WHTE", "RED", "BLUE", "ORNG", "PINK", "YELW", "BRWN"};
	
	private boolean running = false;
	private GameStateManager gsm;
	
	public Game() {
		Dimension size = new Dimension(800, 800);
				
		gsm = new GameStateManager();
		gsm.addGameState(new MainMenuState(gsm));
		
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		setBackground(Color.BLACK);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		addKeyListener(gsm);
		addMouseListener(gsm);
		addMouseMotionListener(gsm);
		
		JFrame f = new JFrame("Mini Golf") {
			private static final long serialVersionUID = 1L;
			
			public void dispose() {
				gsm.dispose();
				super.dispose();
				System.exit(0);
			}
			
		};
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.add(this);
		f.pack();
		f.setLocationRelativeTo(null);
		f.setVisible(true);
		
		running = true;
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.scale(getSize().getWidth() / 400, getSize().getHeight() / 400);
		
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, 400, 400);
		gsm.draw(g);
	}
	
	public void update() {
		gsm.update();
	}
	
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				update();
				delta--;
			}
			repaint();

			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
			}
		}
	}
	
	public static String colorString(Color c) {
		for (int i = 0; i < COLORS.length; i++) {
			if (COLORS[i].equals(c))
				return COLOR_NAMES[i];
		}
		return "";
	}
	
	public static Color stringColor(String s) {
		for (int i = 0; i < COLORS.length; i++) {
			if (COLOR_NAMES[i].equals(s))
				return COLORS[i];
		}
		return null;
	}
	
}
