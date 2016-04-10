package in.vvest.golf;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class RectHill implements Obstacle {

	private Vec2 pos, acc;
	private double width, height;

	public RectHill(Vec2 pos, double width, double height, Vec2 acc) {
		this.pos = pos;
		this.width = width;
		this.height = height;
		this.acc = acc;
	}

	public void draw(Graphics g) {
		g.setColor(Color.GREEN.darker());
		g.fillRect((int) (pos.x), (int) (pos.y), (int) width, (int) height);
		g.setColor(Color.GREEN);
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform old = g2d.getTransform();
		g2d.translate(pos.x + width / 2, pos.y + height / 2);
		g2d.rotate(Math.PI / 2 - acc.angle());
		int[] xPoints = {0, -5, 5};
		int[] yPoints = {10, -5, -5};
		g2d.fillPolygon(xPoints, yPoints, 3);
		g2d.setTransform(old);
	}

	public boolean resolveCollision(Ball b) {
		if (b.getPos().x > pos.x && b.getPos().x < pos.x + width
				&& b.getPos().y > pos.y && b.getPos().y < pos.y + height) {
			b.setVel(b.getVel().add(acc.scale(1d / Ball.UPDATE_INTERVAL)));
			return true;
		}
		return false;
	}

}
