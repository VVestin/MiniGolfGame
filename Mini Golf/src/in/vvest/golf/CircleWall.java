package in.vvest.golf;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;

public class CircleWall implements Obstacle {
	private Vec2 pos;
	private CircleLine outer, inner, end1, end2;
	private double radius, start, end, thickness;

	public CircleWall(Vec2 pos, double radius, double start, double end, double thickness) {
		this.pos = pos;
		this.radius = radius;
		this.start = start;
		this.end = end;
		this.thickness = thickness;
		this.outer = new CircleLine(pos, radius + (thickness /= 2.0), start, end, false);
		this.inner = new CircleLine(pos, radius - thickness, start, end, false);
		this.end1 = new CircleLine(new Vec2(-start).scale(radius).add(pos), thickness, 0.0, 6.283185307179586, true);
		this.end2 = new CircleLine(new Vec2(-end).scale(radius).add(pos), thickness, 0.0, 6.283185307179586, true);
	}

	public void draw(Graphics g) {
		this.end1.draw(g);
		this.end2.draw(g);
		g.setColor(Color.BLACK);
		Area arc = new Area(new Arc2D.Double(this.pos.x - this.outer.getRadius(), this.pos.y - this.outer.getRadius(),
				2.0 * this.outer.getRadius(), 2.0 * this.outer.getRadius(), Math.toDegrees(this.outer.getStart()),
				Math.toDegrees(this.outer.getEnd() - this.outer.getStart()), 2));
		arc.subtract(new Area(new Arc2D.Double(this.pos.x - this.inner.getRadius(), this.pos.y - this.inner.getRadius(),
				2.0 * this.inner.getRadius(), 2.0 * this.inner.getRadius(), Math.toDegrees(this.inner.getStart()),
				Math.toDegrees(this.inner.getEnd() - this.inner.getStart()), 0)));
		((Graphics2D) g).fill(arc);
	}

	public boolean resolveCollision(Ball b) {
		if (!(this.outer.resolveCollision(b) || this.inner.resolveCollision(b) || this.end1.resolveCollision(b)
				|| this.end2.resolveCollision(b))) {
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

	public ObstacleID getID() {
		return ObstacleID.CIRCLE_WALL;
	}
}