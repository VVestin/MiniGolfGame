package in.vvest.leveleditor;

import java.awt.Graphics;
import java.util.Map;

import in.vvest.obstacles.Obstacle;

public interface AdjustablePoint {
	
	public void draw(Graphics g, Obstacle o, boolean selected);
	public void update(Obstacle o, Map<String, Boolean> keyState);
	
}
