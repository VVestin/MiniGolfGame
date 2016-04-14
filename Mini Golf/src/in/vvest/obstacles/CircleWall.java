package in.vvest.obstacles;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.Map;

import in.vvest.golf.Ball;
import in.vvest.golf.Vec2;
import in.vvest.leveleditor.AbstractAdjustablePoint;
import in.vvest.leveleditor.AdjustablePoint;
import in.vvest.leveleditor.TranslationPoint;

public class CircleWall implements Obstacle {
	private Vec2 pos;
	private CircleLine outer, inner, end1, end2;
	private double radius, thickness;
	private int start, end;

	public CircleWall(Vec2 pos, double radius, int start, int end, double thickness) {
		this.pos = pos;
		this.radius = radius;
		thickness /= 2;
		this.thickness = thickness;
		outer = new CircleLine(pos, radius + (thickness), start, end, false);
		inner = new CircleLine(pos, radius - thickness, start, end, false);
		setStart(start);
		setEnd(end);
	}

	public void draw(Graphics g) {
		end1.draw(g);
		end2.draw(g);
		g.setColor(Color.BLACK);
		Area arc = new Area(new Arc2D.Double(pos.x - outer.getRadius(), pos.y - outer.getRadius(),
				2 * outer.getRadius(), 2 * outer.getRadius(), outer.getStart(), outer.getEnd() - outer.getStart(), 2));
		arc.subtract(new Area(new Arc2D.Double(pos.x - inner.getRadius(), pos.y - inner.getRadius(),
				2 * inner.getRadius(), 2 * inner.getRadius(), 0,
				360, 0)));
		((Graphics2D) g).fill(arc);
	}

	public boolean resolveCollision(Ball b) {
		if (!(outer.resolveCollision(b) || inner.resolveCollision(b) || end1.resolveCollision(b)
				|| end2.resolveCollision(b))) {
			return false;
		}
		return true;
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

	public double getThickness() {
		return thickness;
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
					setStart(start + 1);
				} else if (keyState.containsKey("d") && keyState.get("d") && Math.abs(start - 1 - end) <= 360) {
					setStart(start - 1);
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
					setRadius(radius + 1);
				} else if (keyState.containsKey("s") && keyState.get("s")) {
					setRadius(radius - 1);
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
					setEnd(end + 1);
				} else if (keyState.containsKey("d") && keyState.get("d") && Math.abs(end - 1 - start) <= 360) {
					setEnd(end - 1);
				}
			}

			protected Vec2 getPos(Obstacle o) {
				return new Vec2(-Math.toRadians(end)).scale(radius).add(pos);
			}
		});

		return points;
	}

	public ObstacleID getID() {
		return ObstacleID.CIRCLE_WALL;
	}

	public void setPos(Vec2 pos) {
		this.pos = pos;
		outer.setPos(pos);
		inner.setPos(pos);
		setStart(start);
		setEnd(end);
	}
	
	public void setRadius(double radius) {
		this.radius = radius;
		inner.setRadius(radius - thickness);
		outer.setRadius(radius + thickness);
		setStart(start);
		setEnd(end);		
	}

	public void setStart(int start) {
		this.start = start;
		outer.setStart(start);
		inner.setStart(start);
		end1 = new CircleLine(new Vec2(-Math.toRadians(start)).scale(radius).add(pos), thickness, 0, 360, true);
	}

	public void setEnd(int end) {
		this.end = end;
		outer.setEnd(end);
		inner.setEnd(end);
		end2 = new CircleLine(new Vec2(-Math.toRadians(end)).scale(radius).add(pos), thickness, 0, 360, true);
	}
}