package in.vvest.obstacles;

import in.vvest.golf.Ball;
import in.vvest.golf.Vec2;
import in.vvest.leveleditor.AdjustablePoint;
import in.vvest.obstacles.CircleLine;
import in.vvest.obstacles.Line;
import in.vvest.obstacles.Obstacle;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Wall implements Obstacle {
    private Line line1, line2;
    private CircleLine circleA, circleB;
    private Vec2 a, b;
    private double thickness;

    public Wall(Vec2 a, Vec2 b, double thickness) {
        this.a = a;
        this.b = b;
        this.thickness = thickness;
        Vec2 lineNormal = a.subtract(b).normalize();
        thickness /= 2;
        this.line1 = new Line(a.add(lineNormal.rotate(Math.PI / 2).scale(thickness)), b.add(lineNormal.rotate(Math.PI / 2).scale(thickness)));
        this.line2 = new Line(a.add(lineNormal.rotate(-Math.PI / 2).scale(thickness)), b.add(lineNormal.rotate(-Math.PI / 2).scale(thickness)));
        this.circleA = new CircleLine(a, thickness, 0, Math.PI * 2, true);
        this.circleB = new CircleLine(b, thickness, 0, Math.PI * 2, true);
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        int[] xPoints = new int[]{(int)this.line1.getA().x, (int)this.line1.getB().x, (int)this.line2.getB().x, (int)this.line2.getA().x};
        int[] yPoints = new int[]{(int)this.line1.getA().y, (int)this.line1.getB().y, (int)this.line2.getB().y, (int)this.line2.getA().y};
        g.fillPolygon(xPoints, yPoints, 4);
        this.circleA.draw(g);
        this.circleB.draw(g);
    }

    public boolean resolveCollision(Ball b) {
        if (!(this.line1.resolveCollision(b) || this.line2.resolveCollision(b) || this.circleA.resolveCollision(b) || this.circleB.resolveCollision(b))) {
            return false;
        }
        return true;
    }
    
    public Vec2 getA() {
    	return a;
    }
    
    public Vec2 getB() {
    	return b;
    }

	public Vec2 getPos() {
		return a;
	}
    
    public double getThickness() {
    	return thickness;
    }

	public ArrayList<AdjustablePoint> getAdjustmentPoints() {
		return null;
	}
    
    public ObstacleID getID() {
    	return ObstacleID.WALL;
    }

	public void setPos(Vec2 pos) {
		Vec2 diff = pos.subtract(a);
		a = a.add(diff);
		b = b.add(diff);
		circleA.setPos(a);
		circleB.setPos(b);
	}
}