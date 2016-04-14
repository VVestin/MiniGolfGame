package in.vvest.gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.vvest.game.GameStateManager;
import in.vvest.golf.Vec2;
import in.vvest.leveleditor.AdjustableObstacle;
import in.vvest.obstacles.CircleHill;
import in.vvest.obstacles.Obstacle;

public class LevelEditorState extends GameState {

	private ArrayList<Obstacle> obstacles;
	private Map<String, Boolean> keyState;
	private AdjustableObstacle currentObstacle;
	
	public LevelEditorState(GameStateManager gsm) {
		super(gsm);
		obstacles = new ArrayList<Obstacle>();
		keyState = new HashMap<String, Boolean>();
	}

	public void draw(Graphics g) {
		for (int i = 0; i < obstacles.size(); i++) {
			obstacles.get(i).draw(g);
		}
		if (currentObstacle != null)
			currentObstacle.draw(g);
	}

	public void update() {
		if (currentObstacle != null)
			currentObstacle.update(keyState);
		if (isKeyDown("enter") && currentObstacle != null) {
			obstacles.add(currentObstacle.getObstacle());
			currentObstacle = null;
		} else if (currentObstacle == null) {
			if (isKeyDown("1")) {
				currentObstacle = new AdjustableObstacle(new CircleHill(new Vec2(200, 200), 50, 0, 180, 0));
			}
		}
	}

	private boolean isKeyDown(String s) {
		return keyState.containsKey(s) && keyState.get(s);
	}
	
	public void keyPressed(KeyEvent e) {
		int k = e.getKeyCode();
		char c = e.getKeyChar();
		if (c != '?' && k != KeyEvent.VK_ENTER && k != KeyEvent.VK_TAB && k != KeyEvent.VK_SHIFT) {
			keyState.put(String.valueOf(c), true);
		} else if (k == KeyEvent.VK_UP) {
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
		}
	}
	
	public void keyReleased(KeyEvent e) {
		int k = e.getKeyCode();
		char c = e.getKeyChar();
		if (c != '?' && k != KeyEvent.VK_ENTER && k != KeyEvent.VK_TAB && k != KeyEvent.VK_SHIFT) {
			keyState.put(String.valueOf(c), false);
		} else if (k == KeyEvent.VK_UP) {
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
		}
	}
	
}
