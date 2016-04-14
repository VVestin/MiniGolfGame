package in.vvest.obstacles;

import in.vvest.golf.Ball;
import in.vvest.golf.Vec2;
import in.vvest.leveleditor.AdjustablePoint;
import in.vvest.obstacles.Obstacle;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class CircleLine implements Obstacle {
	private Vec2 pos;
	private double radius;
	private int start, end;
	private boolean filled;

	public CircleLine(Vec2 pos, double radius, int start, int end, boolean filled) {
		this.pos = pos;
		this.radius = radius;
		this.start = start;
		this.end = end;
		this.filled = filled;
	}

	public void draw(Graphics g) {
		g.setColor(Color.BLACK);
		if (filled) {
			g.fillArc((int) (pos.x - radius), (int) (pos.y - radius), (int) (radius * 2.0),
					(int) (radius * 2.0), (int) Math.toDegrees(start),
					(int) Math.toDegrees(end - start));
		} else {
			g.drawArc((int) (pos.x - radius), (int) (pos.y - radius), (int) (radius * 2.0),
					(int) (radius * 2.0), (int) Math.toDegrees(start),
					(int) Math.toDegrees(end - start));
		}
	}

	public boolean resolveCollision(Ball b) {
		Vec2 circleDir = pos.subtract(b.getPos()).normalize().rotate(Math.PI);
		Vec2 circlePoint = circleDir.scale(radius).add(pos);
		double angle = -circleDir.angle();
		if (angle < 0) {
			angle += 2 * Math.PI;
		}
		if (circlePoint.distance(b.getPos()) < b.getRadius() && angle > Math.toRadians(start)
				&& angle < Math.toRadians(end)
				&& (b.getPos().distance(pos) < radius
						&& Math.abs(b.getPos().subtract(pos).angle() - b.getVel().angle()) < Math.PI / 2
						|| b.getPos().distance(pos) > radius
								&& Math.abs(b.getPos().subtract(pos).angle() - b.getVel().angle()) > Math.PI / 2)) {
			b.setVel(b.getVel().subtract(circleDir.scale(b.getVel().projectedOn(circleDir) * 2)));
			return true;
		}
		return false;
	}

	public double getRadius() {
		return this.radius;
	}

	public double getStart() {
		return this.start;
	}

	public double getEnd() {
		return this.end;
	}

	public Vec2 getPos() {
		return pos;
	}

	public boolean isFilled() {
		return filled;
	}

	public ArrayList<AdjustablePoint> getAdjustmentPoints() {
		return null;
	}

	public ObstacleID getID() {
		return ObstacleID.CIRCLE_LINE;
	}

	public void setPos(Vec2 pos) {
		this.pos = pos;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public void setEnd(int end) {
		this.end = end;
	}
}