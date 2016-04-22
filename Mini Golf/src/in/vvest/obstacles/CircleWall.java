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

public class CircleWall extends AbstractCircle {
	private CircleLine outer, inner, end1, end2;
	private double thickness;

	public CircleWall(Vec2 pos, double radius, int start, int end, double thickness) {
		super(pos, radius, start, end);
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
		Area arc = new Area(new Arc2D.Double(getPos().x - outer.getRadius(), getPos().y - outer.getRadius(),
				2 * outer.getRadius(), 2 * outer.getRadius(), outer.getStart(), outer.getEnd() - outer.getStart(), 2));
		arc.subtract(new Area(new Arc2D.Double(getPos().x - inner.getRadius(), getPos().y - inner.getRadius(),
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

	public double getThickness() {
		return thickness;
	}

	public ArrayList<AdjustablePoint> getAdjustmentPoints() {
		ArrayList<AdjustablePoint> points = new ArrayList<AdjustablePoint>();
		points.add(new TranslationPoint() {
			public void update(Obstacle o, Map<String, Boolean> keyState) {
				super.update(o, keyState);
				if (keyState.containsKey("q") && keyState.get("q")) {
					setStart(getStart() + 1);
					setEnd(getEnd() + 1);
				} else if (keyState.containsKey("e") && keyState.get("e")) {
					setStart(getStart() - 1);
					setEnd(getEnd() - 1);
				}
			}
		});
		// Adjusts start
		points.add(new AbstractAdjustablePoint() {
			public void update(Obstacle o, Map<String, Boolean> keyState) {
				if (keyState.containsKey("a") && keyState.get("a") && Math.abs(getStart() + 1 - getEnd()) <= 360) {
					setStart(getStart() + 1);
				} else if (keyState.containsKey("d") && keyState.get("d") && Math.abs(getStart() - 1 - getEnd()) <= 360) {
					setStart(getStart() - 1);
				}
			}

			protected Vec2 getPos(Obstacle o) {
				return new Vec2(-Math.toRadians(getStart())).scale(getRadius()).add(o.getPos());
			}
		});
		// Adjusts radius
		points.add(new AbstractAdjustablePoint() {
			public void update(Obstacle o, Map<String, Boolean> keyState) {
				if (keyState.containsKey("w") && keyState.get("w")) {
					setRadius(getRadius() + 1);
				} else if (keyState.containsKey("s") && keyState.get("s")) {
					setRadius(getRadius() - 1);
				}
			}

			protected Vec2 getPos(Obstacle o) {
				return new Vec2(Math.toRadians(-((getEnd() + getStart()) / 2d))).scale(getRadius()).add(o.getPos());
			}
		});

		// Adjusts end
		points.add(new AbstractAdjustablePoint() {
			public void update(Obstacle o, Map<String, Boolean> keyState) {
				if (keyState.containsKey("a") && keyState.get("a") && Math.abs(getEnd() + 1 / 60 - getStart()) <= 360) {
					setEnd(getEnd() + 1);
				} else if (keyState.containsKey("d") && keyState.get("d") && Math.abs(getEnd() - 1 - getStart()) <= 360) {
					setEnd(getEnd() - 1);
				}
			}

			protected Vec2 getPos(Obstacle o) {
				return new Vec2(-Math.toRadians(getEnd())).scale(getRadius()).add(o.getPos());
			}
		});

		return points;
	}

	public ObstacleID getID() {
		return ObstacleID.CIRCLE_WALL;
	}

	public void setPos(Vec2 pos) {
		super.setPos(pos);
		outer.setPos(pos);
		inner.setPos(pos);
		setStart(getStart());
		setEnd(getEnd());
	}
	
	public void setRadius(double radius) {
		super.setRadius(radius);
		inner.setRadius(radius - thickness);
		outer.setRadius(radius + thickness);
		setStart(getStart());
		setEnd(getEnd());		
	}

	public void setStart(int start) {
		super.setStart(start);
		outer.setStart(start);
		inner.setStart(start);
		end1 = new CircleLine(new Vec2(-Math.toRadians(start)).scale(getRadius()).add(getPos()), thickness, 0, 360, true);
	}

	public void setEnd(int end) {
		super.setEnd(end);
		outer.setEnd(end);
		inner.setEnd(end);
		end2 = new CircleLine(new Vec2(-Math.toRadians(end)).scale(getRadius()).add(getPos()), thickness, 0, 360, true);
	}
}