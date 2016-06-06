package in.vvest.gamestates;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.net.DatagramSocket;
import java.util.ArrayList;

import in.vvest.game.Game;
import in.vvest.golf.Console;
import in.vvest.golf.GolfHole;
import in.vvest.golf.OnlinePlayer;
import in.vvest.golf.Player;
import in.vvest.golf.Vec2;
import in.vvest.obstacles.Hole;
import in.vvest.obstacles.Obstacle;
import in.vvest.obstacles.RectGrass;
import in.vvest.obstacles.RectHill;
import in.vvest.obstacles.RectWater;
import in.vvest.obstacles.Wall;
import in.vvest.server.Server;

public class PlayState extends GameState {

	private Player p;
	private GolfHole[] course;
	private Console console;
	private int currentHole;
	private boolean tabPressed;

	public PlayState(Color ballColor, DatagramSocket socket) {
		super();
		course = createGolfCourse();
		p = new OnlinePlayer(course.length, ballColor, Server.ADDRESS, Server.PORT, socket);
		console = new Console((OnlinePlayer) p);
		((OnlinePlayer) p).setConsole(console);
		currentHole = 0;
		resetHole();
	}
	
	public PlayState(GolfHole hole) {
		super();
		course = new GolfHole[1];
		course[0] = hole;
		p = new Player(course.length, Color.WHITE);
		currentHole = 0;
		resetHole();
	}

	public void draw(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 400, 400);
		if (console != null) console.draw(g);
		for (int i = 0; i < course[currentHole].getObstacles().size(); i++)
			course[currentHole].getObstacles().get(i).draw(g);
		ArrayList<Player> players = null;
		if (p instanceof OnlinePlayer) {
			players = ((OnlinePlayer) p).getPlayers();
		
			for (int i = 0; i < players.size(); i++) {
				if (players.get(i).getCurrentHole() == currentHole)
					players.get(i).draw(g);
			}
			
			g.setFont(Game.XTRA_SMALL_FONT);
			g.setColor(Color.WHITE);
			g.drawString("" + players, 5, 20);
		}
		p.draw(g);
		if (tabPressed && p instanceof OnlinePlayer) {
			int[][] scoreCards = new int[players.size() + 1][p.getScoreCard().length];
			scoreCards[0] = p.getScoreCard();
			for (int i = 1; i < players.size(); i++) {
				scoreCards[i] = players.get(i - 1).getScoreCard();
			}
			int cellWidth = 18;
			int width = cellWidth * (course.length + 1) + 50;
			int height = 25 * scoreCards.length;
			int xOffset = 200 - width / 2;
			g.setFont(Game.SMALL_FONT);
			FontMetrics fm = g.getFontMetrics();
			g.setColor(Color.WHITE);
			g.fillRect(xOffset, 150, width, 50 + height);
			g.setColor(Color.BLACK);
			g.fillRect(xOffset + 1, 151, width - 2, 48 + height);
			g.setColor(Color.WHITE);
			g.fillRect(xOffset, 175, width, 1);
			for (int i = 0; i < scoreCards.length; i++) {
				g.fillRect(xOffset, 200 + 25 * i, width, 1);
			}
			g.fillRect(xOffset + 50, 150, 1, 50 + height);
			g.fillRect(xOffset + 50 + course.length * cellWidth, 150, 1, 50 + height);
			g.drawString("Hole", xOffset + (50 - fm.stringWidth("Hole")) / 2, 175 - fm.getHeight() / 2);
			g.drawString("Par", xOffset + (50 - fm.stringWidth("Par")) / 2, 200 - fm.getHeight() / 2);
			g.setColor(p.getColor());
			g.drawString(Game.colorString(p.getColor()), xOffset + (50 - fm.stringWidth(Game.colorString(p.getColor()))) / 2, 225 - fm.getHeight() / 2);
			for (int i = 1; i < scoreCards.length; i++) {
				g.setColor(players.get(i - 1).getColor());
				g.drawString(Game.colorString(players.get(i - 1).getColor()), xOffset + (50 - fm.stringWidth(Game.colorString(players.get(i - 1).getColor()))) / 2, 225 + 25 * i - fm.getHeight() / 2);
			}
			g.setColor(Color.WHITE);
			xOffset += 50;
			int totalPar = 0;
			int[] playerTotals = new int[scoreCards.length];
			for (int i = 0; i < course.length; i++) {
				totalPar += course[i].getPar();
				for (int j = 0; j < scoreCards.length; j++) {
					playerTotals[j] += scoreCards[j][i];
					g.drawString("" + scoreCards[j][i], xOffset + cellWidth * i + (cellWidth - fm.stringWidth("" + scoreCards[j][i])) / 2, 225 + 25 * j - fm.getHeight() / 2);
				}
				g.drawString("" + (i + 1), xOffset + cellWidth * i + (cellWidth - fm.stringWidth("" + (i + 1))) / 2, 175 - fm.getHeight() / 2);
				g.drawString("" + course[i].getPar(), xOffset + cellWidth * i + (cellWidth - fm.stringWidth("" + course[i].getPar())) / 2, 200 - fm.getHeight() / 2);
			}
			g.drawString("T", xOffset + cellWidth * course.length + (cellWidth - fm.stringWidth("T")) / 2, 175 - fm.getHeight() / 2);
			g.drawString("" + totalPar, xOffset + cellWidth * course.length + (cellWidth - fm.stringWidth("" + totalPar)) / 2, 200 - fm.getHeight() / 2);
			for (int i = 0; i < scoreCards.length; i++) {
				g.drawString("" + playerTotals[i], xOffset + cellWidth * course.length + (cellWidth - fm.stringWidth("" + playerTotals[i])) / 2, 225 + 25 * i - fm.getHeight() / 2);
			}
		}
	}

	public void update() {
		p.update(course[currentHole].getObstacles());
		if (p.isInHole()) {
			currentHole++;
			if (currentHole >= course.length)
				currentHole = 0;
			resetHole();
		}
	}
	
	public void dispose() {
		p.dispose();
	}

	private void resetHole() {
		p.getBall().setPos(course[currentHole].getStartPos());
		p.getBall().setInHole(false);
		p.getBall().setVel(new Vec2(0, 0));
		p.setCurrentHole(currentHole);
	}

	public void keyPressed(KeyEvent e) {
		int k = e.getKeyCode();
		if (k == KeyEvent.VK_R)
			resetHole();
		if (k == KeyEvent.VK_TAB)
			tabPressed = true;

		if (console == null || !console.isOpen())
			p.keyPressed(k);
		
		if (console != null) console.keyPressed(e);
	}

	public void keyReleased(KeyEvent e) {
		int k = e.getKeyCode();
		if (k == KeyEvent.VK_TAB)
			tabPressed = false;

		if (console == null || !console.isOpen())
			p.keyReleased(k);
		if (console != null) console.keyReleased(e);
	}

	private static GolfHole[] createGolfCourse() {
		GolfHole[] res = new GolfHole[2];
		ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();
		obstacles.add(new RectGrass(new Vec2(50, 125), 200, 100));
		obstacles.add(new RectGrass(new Vec2(250, 25), 100, 350));
		obstacles.add(new RectWater(new Vec2(250, 25), 100, 102));
		obstacles.add(new Wall(new Vec2(50, 125), new Vec2(250, 125), 4));
		obstacles.add(new Wall(new Vec2(250, 125), new Vec2(250, 25), 4));
		obstacles.add(new Wall(new Vec2(250, 25), new Vec2(350, 25), 4));
		obstacles.add(new Wall(new Vec2(350, 25), new Vec2(350, 375), 4));
		obstacles.add(new Wall(new Vec2(350, 375), new Vec2(250, 375), 4));
		obstacles.add(new Wall(new Vec2(250, 375), new Vec2(250, 225), 4));
		obstacles.add(new Wall(new Vec2(250, 225), new Vec2(50, 225), 4));
		obstacles.add(new Wall(new Vec2(50, 225), new Vec2(50, 125), 4));
		obstacles.add(new Wall(new Vec2(150, 150), new Vec2(150, 200), 4));
		obstacles.add(new Hole(new Vec2(100, 175)));
		res[0] = new GolfHole(obstacles, new Vec2(300, 360), 3);
		obstacles = new ArrayList<Obstacle>();
		obstacles.add(new RectGrass(new Vec2(75, 50), 250, 250));
		obstacles.add(new RectGrass(new Vec2(75, 300), 175, 50));
		obstacles.add(new RectHill(new Vec2(150, 125), 100, 165, new Vec2(0, .1)));
		obstacles.add(new RectHill(new Vec2(250, 125), 75, 125, new Vec2(0, -.1)));
		obstacles.add(new Wall(new Vec2(325, 300), new Vec2(325, 50), 4));
		obstacles.add(new Wall(new Vec2(325, 50), new Vec2(75, 50), 4));
		obstacles.add(new Wall(new Vec2(75, 50), new Vec2(75, 350), 4));
		obstacles.add(new Wall(new Vec2(75, 350), new Vec2(250, 350), 4));
		obstacles.add(new Wall(new Vec2(150, 125), new Vec2(150, 300), 4));
		obstacles.add(new Wall(new Vec2(250, 125), new Vec2(250, 150), 4));
		obstacles.add(new Wall(new Vec2(250, 175), new Vec2(250, 350), 4));
		obstacles.add(new Wall(new Vec2(250, 300), new Vec2(325, 300), 4));
		obstacles.add(new Wall(new Vec2(150, 125), new Vec2(250, 125), 4));
		obstacles.add(new Wall(new Vec2(125, 50), new Vec2(75, 100), 4));
		obstacles.add(new Wall(new Vec2(125, 350), new Vec2(75, 300), 4));
		obstacles.add(new Hole(new Vec2(200, 325)));
		res[1] = new GolfHole(obstacles, new Vec2(292.5, 275), 3);
		return res;
	}
}

