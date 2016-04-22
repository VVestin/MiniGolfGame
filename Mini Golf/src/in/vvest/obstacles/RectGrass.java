package in.vvest.obstacles;

import java.awt.Color;
import java.awt.Graphics;

import in.vvest.golf.Ball;
import in.vvest.golf.Vec2;

public class RectGrass extends AbstractRect {
	
	public RectGrass(Vec2 pos, double width, double height) {
		super(pos, width, height);
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.GREEN);
		g.fillRect((int) getPos().x, (int) getPos().y, (int) getWidth(), (int) getHeight()); 
	}

	public boolean resolveCollision(Ball b) {
		return false;
	}
    
    public ObstacleID getID() {
    	return ObstacleID.RECT_GRASS;
    }
}
