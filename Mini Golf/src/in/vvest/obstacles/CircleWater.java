package in.vvest.obstacles;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Map;

import in.vvest.golf.Ball;
import in.vvest.golf.Vec2;
import in.vvest.leveleditor.AbstractAdjustablePoint;
import in.vvest.leveleditor.AdjustablePoint;
import in.vvest.leveleditor.TranslationPoint;

public class CircleWater extends AbstractCircle {
	private double acc;

	public CircleWater(Vec2 pos, double radius, int start, int end, double acc) {
		super(pos, radius, start, end);
		this.acc = acc;
	}

	public void draw(Graphics g) {
		g.setColor(Color.GREEN.darker());
		g.fillArc((int) (getPos().x - getRadius()), (int) (getPos().y - getRadius()), (int) (getRadius() * 2.0),
				(int) (getRadius() * 2.0), getStart(), getEnd() - getStart());
		Vec2 arrowLoc = new Vec2(Math.toRadians(-((getEnd() + getStart()) / 2d))).scale(getRadius() / 2).add(getPos());
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.GREEN);
		AffineTransform old = g2d.getTransform();
		g2d.translate(arrowLoc.x, arrowLoc.y);
		g2d.rotate(arrowLoc.subtract(getPos()).rotate(Math.PI / 2 * (acc > 0 ? 1 : -1)).angle());
		int[] xPoints = {0, -3, 3};
		int[] yPoints = {6, -3, -3};
		g2d.fillPolygon(xPoints, yPoints, 3);
		g2d.setTransform(old);	
	}

	public boolean resolveCollision(Ball b) {
		Vec2 circleDir = this.getPos().subtract(b.getPos()).normalize().rotate(180);
		double angle = -circleDir.angle();
		if (angle < 0) {
			angle += 360;
		}
		if (getPos().distance(b.getPos()) < getRadius() && getPos().distance(b.getPos()) > 1 && angle > getStart()
				&& angle < this.getEnd()) {
			b.setPos(b.getLastHitPos());
			b.setVel(new Vec2(0, 0));
			return true;
		}
		return false;
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
				} else if (keyState.containsKey("d") && keyState.get("d")
						&& Math.abs(getStart() - 1 - getEnd()) <= 360) {
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
				if (keyState.containsKey("a") && keyState.get("a")
						&& Math.abs(getEnd() + 1 / 60 - getStart()) <= 360) {
					setEnd(getEnd() + 1);
				} else if (keyState.containsKey("d") && keyState.get("d")
						&& Math.abs(getEnd() - 1 - getStart()) <= 360) {
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
		return ObstacleID.CIRCLE_HILL;
	}
}
