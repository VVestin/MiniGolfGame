package in.vvest.obstacles;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import in.vvest.golf.Ball;
import in.vvest.golf.Vec2;
import in.vvest.leveleditor.AdjustablePoint;

public class Hole implements Obstacle {

	private Vec2 pos;
	private double radius;
	
	public Hole(Vec2 pos, double radius) {
		this.pos = pos;
		this.radius = radius;
	}
	
	public void draw(Graphics g) {	
		g.setColor(Color.BLACK);
		g.fillOval((int) (pos.x - radius), (int) (pos.y - radius), (int) (radius * 2), (int) (radius * 2));
	}
	
	public boolean resolveCollision(Ball b) {
		if (b.getPos().distance(pos) < b.getRadius() * .6 && b.getVel().lengthSquared() < 25) {
			b.setInHole(true);
			return true;
		} else if (b.getPos().distance(pos) < b.getRadius() * .75 + radius) {
			b.setVel(b.getVel().subtract(b.getPos().subtract(this.pos).normalize().scale(.03 / Ball.UPDATE_INTERVAL)));
		}
		return false;
	}
	
	public Vec2 getPos() {
		return pos;
	}
	
	public double getRadius() {
		return radius;
	}

	public ArrayList<AdjustablePoint> getAdjustmentPoints() {
		return null;
	}
    
    public ObstacleID getID() {
    	return ObstacleID.HOLE;
    }
	
	public void setPos(Vec2 pos) {
		this.pos = pos;
	}
}
