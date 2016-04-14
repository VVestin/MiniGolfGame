package in.vvest.obstacles;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.util.ArrayList;

import in.vvest.golf.Ball;
import in.vvest.golf.Vec2;
import in.vvest.leveleditor.AdjustablePoint;

public class CircleWall implements Obstacle {
	private Vec2 pos;
	private CircleLine outer, inner, end1, end2;
	private double radius, thickness;
	private int start, end;

	public CircleWall(Vec2 pos, double radius, int start, int end, double thickness) {
		this.pos = pos;
		this.radius = radius;
		this.start = start;
		this.end = end;
		thickness /= 2;
		this.thickness = thickness;
		outer = new CircleLine(pos, radius + (thickness), start, end, false);
		inner = new CircleLine(pos, radius - thickness, start, end, false);
		end1 = new CircleLine(new Vec2(-Math.toRadians(start)).scale(radius).add(pos), thickness, 0, 360, true);
		end2 = new CircleLine(new Vec2(-Math.toRadians(end)).scale(radius).add(pos), thickness, 0, 360, true);
	}

	public void draw(Graphics g) {
		end1.draw(g);
		end2.draw(g);
		g.setColor(Color.BLACK);
		Area arc = new Area(new Arc2D.Double(pos.x - outer.getRadius(), pos.y - outer.getRadius(),
				2 * outer.getRadius(), 2 * outer.getRadius(), outer.getStart(),
				Math.toDegrees(outer.getEnd() - outer.getStart()), 2));
		arc.subtract(new Area(new Arc2D.Double(pos.x - inner.getRadius(), pos.y - inner.getRadius(),
				2 * inner.getRadius(), 2 * inner.getRadius(), inner.getStart(),
				Math.toDegrees(inner.getEnd() - inner.getStart()), 0)));
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
		return null;
	}

	public ObstacleID getID() {
		return ObstacleID.CIRCLE_WALL;
	}
	
	public void setPos(Vec2 pos) {
		this.pos = pos;
		outer.setPos(pos);
		inner.setPos(pos);
		end1 = new CircleLine(new Vec2(-Math.toRadians(start)).scale(radius).add(pos), thickness, 0, 360, true);
		end2 = new CircleLine(new Vec2(-Math.toRadians(end)).scale(radius).add(pos), thickness, 0, 360, true);
	}
}