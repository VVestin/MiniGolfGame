package in.vvest.golf;

import java.awt.Color;
import java.awt.Graphics;

public class RectGrass implements Obstacle {

	private Vec2 pos;
	private double width, height;
	
	public RectGrass(Vec2 pos, double width, double height) {
		this.pos = pos;
		this.width = width;
		this.height = height;
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.GREEN);
		g.fillRect((int) pos.x, (int) pos.y, (int) width, (int) height); 
	}

	public boolean resolveCollision(Ball b) {
		return false;
	}
	
	public Vec2 getPos() {
		return pos;
	}
	
	public double getWidth() {
		return width;
	}
	
	public double getHeight() {
		return height;
	}
    
    public ObstacleID getID() {
    	return ObstacleID.RECT_GRASS;
    }
}
