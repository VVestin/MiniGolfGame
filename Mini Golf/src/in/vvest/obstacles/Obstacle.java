package in.vvest.obstacles;

import java.awt.Graphics;
import java.util.ArrayList;

import in.vvest.golf.Ball;
import in.vvest.golf.Vec2;
import in.vvest.leveleditor.AdjustablePoint;

public interface Obstacle {

	public void draw(Graphics g);
	public boolean resolveCollision(Ball b);
	public Vec2 getPos();
	public void setPos(Vec2 pos);
	public ObstacleID getID();
	public ArrayList<AdjustablePoint> getAdjustmentPoints();
	
}
