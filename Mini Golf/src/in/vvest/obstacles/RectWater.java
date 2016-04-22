package in.vvest.obstacles;

import java.awt.Color;
import java.awt.Graphics;

import in.vvest.golf.Ball;
import in.vvest.golf.Vec2;

public class RectWater extends AbstractRect {

	public RectWater(Vec2 pos, double width, double height) {
		super(pos, width, height);
	}

	public void draw(Graphics g) {
		g.setColor(Color.BLUE.brighter());
		g.fillRect((int) getPos().x, (int) getPos().y, (int) getWidth(), (int) getHeight());
	}

	public boolean resolveCollision(Ball b) {
		if (b.getPos().x > getPos().x && b.getPos().x < getPos().x + getWidth() && b.getPos().y > getPos().y
				&& b.getPos().y < getPos().y + getHeight()) {
			b.setPos(b.getLastHitPos());
			b.setVel(new Vec2(0, 0));
			return true;
		}
		return false;
	}

	public ObstacleID getID() {
		return ObstacleID.RECT_GRASS;
	}
}
