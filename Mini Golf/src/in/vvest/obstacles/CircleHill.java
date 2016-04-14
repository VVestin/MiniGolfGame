package in.vvest.obstacles;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Map;

import in.vvest.golf.Ball;
import in.vvest.golf.Vec2;
import in.vvest.leveleditor.AbstractAdjustablePoint;
import in.vvest.leveleditor.AdjustablePoint;
import in.vvest.leveleditor.TranslationPoint;

public class CircleHill implements Obstacle {
	private Vec2 pos;
	private double radius, start, end, acc;

	public CircleHill(Vec2 pos, double radius, double start, double end, double acc) {
		this.pos = pos;
		this.radius = radius;
		this.start = start;
		this.end = end;
		this.acc = acc;
	}

	public void draw(Graphics g) {
		g.setColor(Color.GREEN.darker());
		g.fillArc((int) (this.pos.x - this.radius), (int) (this.pos.y - this.radius), (int) (this.radius * 2.0),
				(int) (this.radius * 2.0), (int) Math.toDegrees(this.start),
				(int) Math.toDegrees(this.end - this.start));
	}

	public boolean resolveCollision(Ball b) {
		Vec2 circleDir = this.pos.subtract(b.getPos()).normalize().rotate(3.141592653589793);
		double angle = -circleDir.angle();
		if (angle < 0.0) {
			angle += Math.PI * 2;
		}
		if (this.pos.distance(b.getPos()) < this.radius && this.pos.distance(b.getPos()) > 1.0 && angle > this.start
				&& angle < this.end) {
			b.setVel(b.getVel().subtract(b.getPos().subtract(this.pos).normalize().scale(this.acc / 25.0)));
			return true;
		}
		return false;
	}

	public Vec2 getPos() {
		return pos;
	}

	public double getRadius() {
		return radius;
	}

	public double getStart() {
		return start;
	}

	public double getEnd() {
		return end;
	}

	public double getAcc() {
		return acc;
	}

	public ArrayList<AdjustablePoint> getAdjustmentPoints() {
		ArrayList<AdjustablePoint> points = new ArrayList<AdjustablePoint>();
		points.add(new TranslationPoint());
		// Adjusts start
		points.add(new AbstractAdjustablePoint() {
			public void update(Obstacle o, Map<String, Boolean> keyState) {
				if (keyState.containsKey("a") && keyState.get("a")) {
					start += Math.PI / 60;
				} else if (keyState.containsKey("d") && keyState.get("d")) {
					start -= Math.PI / 60;
				}
			}

			protected Vec2 getPos(Obstacle o) {
				return new Vec2(-start).scale(radius).add(pos);
			}
		});
		// Adjusts end
		points.add(new AbstractAdjustablePoint() {
			public void update(Obstacle o, Map<String, Boolean> keyState) {
				if (keyState.containsKey("a") && keyState.get("a")) {
					end += Math.PI / 60;
				} else if (keyState.containsKey("d") && keyState.get("d")) {
					end -= Math.PI / 60;
				}
			}

			protected Vec2 getPos(Obstacle o) {
				return new Vec2(-end).scale(radius).add(pos);
			}
		});
		return points;
	}

	public ObstacleID getID() {
		return ObstacleID.CIRCLE_HILL;
	}

	public void setPos(Vec2 pos) {
		this.pos = pos;
	}
}