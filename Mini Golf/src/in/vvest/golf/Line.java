package in.vvest.golf;

import java.awt.Color;
import java.awt.Graphics;

public class Line implements Obstacle {

	private Vec2 a, b;

	public Line(Vec2 a, Vec2 b) {
		this.a = a;
		this.b = b;
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawLine((int) (a.x), (int) (a.y), (int) (b.x), (int) (b.y));
	}

	public boolean resolveCollision(Ball ball) {
		Vec2 lineNormal = new Vec2(a.subtract(b).angle() + Math.PI / 2);
		double distance = ball.getPos().subtract(a).dotProduct(lineNormal);
		double distanceOnLine = ball.getPos().subtract(b).projectedOn(a.subtract(b));
		boolean ballAboveLine = ball.getPos().y > (a.subtract(b).y / a.subtract(b).x) * (ball.getPos().x - a.x) + a.y;
		boolean ballWillBeAboveLine = ball.getPos().add(ball.getVel().scale(2 * a.subtract(b).length())).y > (a.subtract(b).y / a.subtract(b).x) * (ball.getPos().add(ball.getVel().scale(2 * a.subtract(b).length())).x - a.x) + a.y;
		if (Math.abs(distance) < ball.getRadius() && distanceOnLine + ball.getRadius() < a.subtract(b).length() && distanceOnLine > 0
				&& ballAboveLine != ballWillBeAboveLine) {			
			ball.setVel(ball.getVel().subtract(lineNormal.scale(ball.getVel().projectedOn(lineNormal) * 2)));
			return true;
		}
		return false;
	}

	public Vec2 getA() {
		return a;
	}
	
	public Vec2 getB() {
		return b;
	}
	
}
