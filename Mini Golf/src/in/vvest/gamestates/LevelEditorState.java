package in.vvest.gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.vvest.golf.GolfHole;
import in.vvest.golf.Vec2;
import in.vvest.leveleditor.AdjustableObstacle;
import in.vvest.leveleditor.MenuBar;
import in.vvest.leveleditor.MenuBarLabel;
import in.vvest.obstacles.CircleHill;
import in.vvest.obstacles.CircleWall;
import in.vvest.obstacles.Hole;
import in.vvest.obstacles.Obstacle;
import in.vvest.obstacles.RectGrass;

public class LevelEditorState extends GameState {
	private ArrayList<Obstacle> obstacles;
	private Map<String, Boolean> keyState;
	private AdjustableObstacle currentObstacle;
	private MenuBar menuBar;
	
	public LevelEditorState() {
		super();
		obstacles = new ArrayList<Obstacle>();
		keyState = new HashMap<String, Boolean>();
		currentObstacle = null;
		menuBar = new MenuBar(0);
		menuBar.addItem(new MenuBarLabel("1 - CircleHill"));
		menuBar.addItem(new MenuBarLabel("2 - CircleWall"));
		menuBar.addItem(new MenuBarLabel("3 - RectGrass"));
		menuBar.addItem(new MenuBarLabel("4 - Hole"));
	}

	public void draw(Graphics g) {
		for (int i = 0; i < obstacles.size(); i++) {
			obstacles.get(i).draw(g);
		}
		if (currentObstacle != null)
			currentObstacle.draw(g);
		else
			menuBar.draw(g);
	}

	public void update() {
		if (currentObstacle != null)
			currentObstacle.update(keyState);
		if (isKeyDown("enter") && currentObstacle != null) {
			obstacles.add(currentObstacle.getObstacle());
			currentObstacle = null;
		} else if (isKeyDown("p")) {
			setGameState(new PlayState(new GolfHole(obstacles, new Vec2(350, 350), 2)));
		} else if (currentObstacle == null) {
			if (isKeyDown("1")) {
				currentObstacle = new AdjustableObstacle(new CircleHill(new Vec2(200, 200), 50, 0, 180, 0));
			} else if (isKeyDown("2")) {
				currentObstacle = new AdjustableObstacle(new CircleWall(new Vec2(200, 200), 50, 0, 180, 4));
			} else if (isKeyDown("3")) {
				currentObstacle = new AdjustableObstacle(new RectGrass(new Vec2(25, 25), 350, 350));
			} else if (isKeyDown("4")) {
				currentObstacle = new AdjustableObstacle(new Hole(new Vec2(200, 200)));
			}
		} else if (isKeyDown("escape")) {
			currentObstacle = null;
		}
	}

	private boolean isKeyDown(String s) {
		return keyState.containsKey(s) && keyState.get(s);
	}
	
	public void keyPressed(KeyEvent e) {
		int k = e.getKeyCode();
		char c = e.getKeyChar();
		if (k == KeyEvent.VK_UP) {
			keyState.put("up", true);
		} else if (k == KeyEvent.VK_DOWN) {
			keyState.put("down", true);
		} else if (k == KeyEvent.VK_LEFT) {
			keyState.put("left", true);
		} else if (k == KeyEvent.VK_RIGHT) {
			keyState.put("right", true);
		} else if (k == KeyEvent.VK_ENTER) {
			keyState.put("enter", true);
		} else if (k == KeyEvent.VK_TAB) {
			keyState.put("tab", true);
		} else if (k == KeyEvent.VK_SHIFT) {
			keyState.put("shift", true);
		} else if (k == KeyEvent.VK_ESCAPE) {
			keyState.put("escape", true);
		} else if (c != '?') {
			keyState.put(String.valueOf(c), true);
		}
	}
	
	public void keyReleased(KeyEvent e) {
		int k = e.getKeyCode();
		char c = e.getKeyChar();
		if (k == KeyEvent.VK_UP) {
			keyState.put("up", false);
		} else if (k == KeyEvent.VK_DOWN) {
			keyState.put("down", false);
		} else if (k == KeyEvent.VK_LEFT) {
			keyState.put("left", false);
		} else if (k == KeyEvent.VK_RIGHT) {
			keyState.put("right", false);
		} else if (k == KeyEvent.VK_ENTER) {
			keyState.put("enter", false);
		} else if (k == KeyEvent.VK_TAB) {
			keyState.put("tab", false);
		} else if (k == KeyEvent.VK_SHIFT) {
			keyState.put("shift", false);
		} else if (k == KeyEvent.VK_ESCAPE) {
			keyState.put("escape", false);
		} else if (c != '?') {
			keyState.put(String.valueOf(c), false);
		}
	}
}
