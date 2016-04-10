package in.vvest.game;

import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import in.vvest.gamestates.GameState;

public class GameStateManager extends KeyAdapter implements MouseListener, MouseMotionListener {
	
	private int currentState;
	private List<GameState> gameStates;
	
	public GameStateManager() {
		gameStates = new ArrayList<GameState>();
	}
	
	public void setCurrentGameState(int i) {
		currentState = i;
	}
	
	public int getCurrentGameState() {
		return currentState;
	}
	
	public void setGameState(int i, GameState gs) {
		gameStates.set(i, gs);
	}
	
	public void addGameState(GameState gs) {
		gameStates.add(gs);
	}
	
	public void draw(Graphics g) {
		gameStates.get(currentState).draw(g);
	}
	
	public void update() {
		gameStates.get(currentState).update();
	}
	
	public void dispose() {
		for (int i = 0; i < gameStates.size(); i++) {
			if (gameStates.get(i) != null) gameStates.get(i).dispose();
		}
	}
	
	public void keyPressed(KeyEvent e) {
		gameStates.get(currentState).keyPressed(e);;
	}
	
	public void keyReleased(KeyEvent e) {
		gameStates.get(currentState).keyReleased(e);
	}

	public void mouseClicked(MouseEvent e) {
		gameStates.get(currentState).mouseClicked(e);
	}

	public void mouseEntered(MouseEvent e) {
		gameStates.get(currentState).mouseEntered(e);		
	}

	public void mouseExited(MouseEvent e) {
		gameStates.get(currentState).mouseExited(e);		
	}

	public void mousePressed(MouseEvent e) {
		gameStates.get(currentState).mousePressed(e);		
	}

	public void mouseReleased(MouseEvent e) {
		gameStates.get(currentState).mouseReleased(e);		
	}

	public void mouseDragged(MouseEvent e) {
		gameStates.get(currentState).mouseDragged(e);		
	}

	public void mouseMoved(MouseEvent e) {
		gameStates.get(currentState).mouseMoved(e);		
	}
}
