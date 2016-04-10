package in.vvest.golf;

import java.awt.Graphics;

public interface Obstacle {

	public void draw(Graphics g);
	public boolean resolveCollision(Ball b);
	
}
