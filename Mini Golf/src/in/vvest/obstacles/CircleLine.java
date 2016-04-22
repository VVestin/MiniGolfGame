package in.vvest.obstacles;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import in.vvest.golf.Ball;
import in.vvest.golf.Vec2;
import in.vvest.leveleditor.AdjustablePoint;

public class CircleLine extends AbstractCircle {
	private boolean filled;

	public CircleLine(Vec2 pos, double radius, int start, int end, boolean filled) {
		super(pos, radius, start, end);
		this.filled = filled;
	}

	public void draw(Graphics g) {
		g.setColor(Color.BLACK);
		if (filled) {
			g.fillArc((int) (getPos().x - getRadius()), (int) (getPos().y - getRadius()), (int) (getRadius() * 2.0),
					(int) (getRadius() * 2.0), (int) Math.toDegrees(getStart()),
					(int) Math.toDegrees(getEnd() - getStart()));
		} else {
			g.drawArc((int) (getPos().x - getRadius()), (int) (getPos().y - getRadius()), (int) (getRadius() * 2.0),
					(int) (getRadius() * 2.0), (int) Math.toDegrees(getStart()),
					(int) Math.toDegrees(getEnd() - getStart()));
		}
	}

	public boolean resolveCollision(Ball b) {
		Vec2 circleDir = getPos().subtract(b.getPos()).normalize().rotate(Math.PI);
		Vec2 circlePoint = circleDir.scale(getRadius()).add(getPos());
		double angle = -circleDir.angle();
		if (angle < 0) {
			angle += 2 * Math.PI;
		}
		if (circlePoint.distance(b.getPos()) < b.getRadius() && angle > Math.toRadians(getStart())
				&& angle < Math.toRadians(getEnd())
				&& (b.getPos().distance(getPos()) < getRadius()
						&& Math.abs(b.getPos().subtract(getPos()).angle() - b.getVel().angle()) < Math.PI / 2
						|| b.getPos().distance(getPos()) > getRadius()
								&& Math.abs(b.getPos().subtract(getPos()).angle() - b.getVel().angle()) > Math.PI / 2)) {
			b.setVel(b.getVel().subtract(circleDir.scale(b.getVel().projectedOn(circleDir) * 2)));
			return true;
		}
		return false;
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
}