package in.vvest.obstacles;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import in.vvest.golf.Ball;
import in.vvest.golf.Vec2;

public class RectHill extends AbstractRect {

	private Vec2 acc;
	
	public RectHill(Vec2 pos, double width, double height, Vec2 acc) {
		super(pos, width, height);
		this.acc = acc;
	}

	public void draw(Graphics g) {
		g.setColor(Color.GREEN.darker());
		g.fillRect((int) (getPos().x), (int) (getPos().y), (int) getWidth(), (int) getHeight());
		g.setColor(Color.GREEN);
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform old = g2d.getTransform();
		g2d.translate(getPos().x + getWidth() / 2, getPos().y + getHeight() / 2);
		g2d.rotate(Math.PI / 2 - acc.angle());
		int[] xPoints = {0, -5, 5};
		int[] yPoints = {10, -5, -5};
		g2d.fillPolygon(xPoints, yPoints, 3);
		g2d.setTransform(old);
	}

	public boolean resolveCollision(Ball b) {
		if (b.getPos().x > getPos().x && b.getPos().x < getPos().x + getWidth()
				&& b.getPos().y > getPos().y && b.getPos().y < getPos().y + getHeight()) {
			b.setVel(b.getVel().add(acc.scale(1d / Ball.UPDATE_INTERVAL)));
			return true;
		}
		return false;
	}
	
	public Vec2 getAcc() {
		return acc;
	}
	
    public ObstacleID getID() {
    	return ObstacleID.RECT_HILL;
    }
}
