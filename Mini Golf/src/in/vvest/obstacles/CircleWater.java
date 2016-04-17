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

public class CircleWater implements Obstacle {
	private Vec2 pos;
	private double radius, acc;
	private int start, end;

	public CircleWater(Vec2 pos, double radius, int start, int end, double acc) {
		this.pos = pos;
		this.radius = radius;
		this.start = start;
		this.end = end;
		this.acc = acc;
	}

	public void draw(Graphics g) {
		g.setColor(Color.GREEN.darker());
		g.fillArc((int) (this.pos.x - this.radius), (int) (this.pos.y - this.radius), (int) (this.radius * 2.0),
				(int) (this.radius * 2.0), this.start, this.end - this.start);
	}

	public boolean resolveCollision(Ball b) {
		Vec2 circleDir = this.pos.subtract(b.getPos()).normalize().rotate(180);
		double angle = -circleDir.angle();
		if (angle < 0) {
			angle += 360;
		}
		if (this.pos.distance(b.getPos()) < this.radius && this.pos.distance(b.getPos()) > 1 && angle > this.start
				&& angle < this.end) {
			b.setPos(b.getLastHitPos());
			b.setVel(new Vec2(0, 0));
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
		points.add(new TranslationPoint() {
			public void update(Obstacle o, Map<String, Boolean> keyState) {
				super.update(o, keyState);
				if (keyState.containsKey("q") && keyState.get("q")) {
					start += 1;
					end += 1;
				} else if (keyState.containsKey("e") && keyState.get("e")) {
					start -= 1;
					end -= 1;
				}
			}
		});
		// Adjusts start
		points.add(new AbstractAdjustablePoint() {
			public void update(Obstacle o, Map<String, Boolean> keyState) {
				if (keyState.containsKey("a") && keyState.get("a") && Math.abs(start + 1 - end) <= 360) {
					start += 1;
				} else if (keyState.containsKey("d") && keyState.get("d") && Math.abs(start - 1 - end) <= 360) {
					start -= 1;
				}
			}

			protected Vec2 getPos(Obstacle o) {
				return new Vec2(-Math.toRadians(start)).scale(radius).add(pos);
			}
		});
		// Adjusts radius
		points.add(new AbstractAdjustablePoint() {
			public void update(Obstacle o, Map<String, Boolean> keyState) {
				if (keyState.containsKey("w") && keyState.get("w")) {
					radius++;
				} else if (keyState.containsKey("s") && keyState.get("s")) {
					radius--;
				}
			}

			protected Vec2 getPos(Obstacle o) {
				return new Vec2(Math.toRadians(-((end + start) / 2d))).scale(radius).add(pos);
			}
		});

		// Adjusts end
		points.add(new AbstractAdjustablePoint() {
			public void update(Obstacle o, Map<String, Boolean> keyState) {
				if (keyState.containsKey("a") && keyState.get("a") && Math.abs(end + 1 / 60 - start) <= 360) {
					end += 1;
				} else if (keyState.containsKey("d") && keyState.get("d") && Math.abs(end - 1 - start) <= 360) {
					end -= 1;
				}
			}

			protected Vec2 getPos(Obstacle o) {
				return new Vec2(-Math.toRadians(end)).scale(radius).add(pos);
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
