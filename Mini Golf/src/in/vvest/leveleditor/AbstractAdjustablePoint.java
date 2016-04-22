package in.vvest.leveleditor;

import java.awt.Color;
import java.awt.Graphics;

import in.vvest.golf.Vec2;
import in.vvest.obstacles.Obstacle;

public abstract class AbstractAdjustablePoint implements AdjustablePoint {

	public void draw(Graphics g, Obstacle o, boolean selected) {
		if (selected)
			g.setColor(Color.YELLOW);
		else
			g.setColor(Color.GRAY);				
		g.fillOval((int) (getPos(o).x - 2), (int) (getPos(o).y - 2), 4, 4);
	}
	
	protected abstract Vec2 getPos(Obstacle o);

}
