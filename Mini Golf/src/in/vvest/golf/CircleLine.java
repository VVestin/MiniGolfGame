package in.vvest.golf;

import in.vvest.golf.Ball;
import in.vvest.golf.Obstacle;
import in.vvest.golf.Vec2;
import java.awt.Color;
import java.awt.Graphics;

public class CircleLine implements Obstacle {
    private Vec2 pos;
    private double radius, start, end;
    private boolean filled;

    public CircleLine(Vec2 pos, double radius, double start, double end, boolean filled) {
        this.pos = pos;
        this.radius = radius;
        this.start = start;
        this.end = end;
        this.filled = filled;
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        if (this.filled) {
            g.fillArc((int)(this.pos.x - this.radius), (int)(this.pos.y - this.radius), (int)(this.radius * 2.0), (int)(this.radius * 2.0), (int)Math.toDegrees(this.start), (int)Math.toDegrees(this.end - this.start));
        } else {
            g.drawArc((int)(this.pos.x - this.radius), (int)(this.pos.y - this.radius), (int)(this.radius * 2.0), (int)(this.radius * 2.0), (int)Math.toDegrees(this.start), (int)Math.toDegrees(this.end - this.start));
        }
    }

    public boolean resolveCollision(Ball b) {
        Vec2 circleDir = this.pos.subtract(b.getPos()).normalize().rotate(3.141592653589793);
        Vec2 circlePoint = circleDir.scale(this.radius).add(this.pos);
        double angle = - circleDir.angle();
        if (angle < 0.0) {
            angle += 6.283185307179586;
        }
        if (circlePoint.distance(b.getPos()) < b.getRadius() && angle > this.start && angle < this.end) {
            b.setVel(b.getVel().subtract(circleDir.scale(b.getVel().projectedOn(circleDir) * 2.0)));
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
}