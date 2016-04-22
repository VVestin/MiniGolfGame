package in.vvest.obstacles;

import in.vvest.golf.Vec2;

public abstract class AbstractCircle implements Obstacle {

	private Vec2 pos;
	private double radius;
	private int start, end;
	
	public AbstractCircle(Vec2 pos, double radius, int start, int end) {
		this.pos = pos;
		this.radius = radius;
		this.start = start;
		this.end = end;
	}
		
	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public Vec2 getPos() {
		return pos;
	}

	public void setPos(Vec2 pos) {
		this.pos = pos;
	}
}
