package in.vvest.golf;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;
import java.util.List;

import in.vvest.obstacles.CircleHill;
import in.vvest.obstacles.Obstacle;
import in.vvest.obstacles.RectHill;

public class Ball implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final int UPDATE_INTERVAL = 25;
	
	private Vec2 pos, vel;
	private double radius;
	private boolean inHole;

	public Ball(Vec2 pos, double radius) {
		this.pos = pos;
		this.radius = radius;
		vel = new Vec2(0, 0);
		inHole = false;
	}

	public void draw(Graphics g, Color color) {
		if (!inHole) {
			g.setColor(color);
			g.fillOval((int) (pos.x - radius), (int) (pos.y - radius), (int) (radius * 2), (int) (radius * 2));
			g.setColor(Color.BLACK);
			g.drawOval((int) (pos.x - radius), (int) (pos.y - radius), (int) (radius * 2), (int) (radius * 2));
		}
	}

	public boolean isInHole() {
		return inHole;
	}

	public void setInHole(boolean inHole) {
		this.inHole = inHole;
	}

	public void update(List<Obstacle> obstacles) {
		if (!inHole) {
			vel = vel.scale(.98);
			boolean onHill = false;
			for (int i = 0; i < UPDATE_INTERVAL; i++) {
				pos = pos.add(vel.scale(1d / UPDATE_INTERVAL));
				for (int j = 0; j < obstacles.size(); j++) {
					boolean colliding = obstacles.get(j).resolveCollision(this);
					if (!onHill && colliding && (obstacles.get(j) instanceof RectHill || obstacles.get(j) instanceof CircleHill)) 
						onHill = true;
				}
			}
			if (!onHill && vel.length() < .005)
				vel = new Vec2(0, 0);
		} else {
			vel = new Vec2(300, 300);
		}
	}

	public Vec2 getPos() {
		return pos;
	}

	public void setPos(Vec2 pos) {
		this.pos = pos;
	}

	public Vec2 getVel() {
		return vel;
	}

	public void setVel(Vec2 vel) {
		this.vel = vel;
	}

	public double getRadius() {
		return radius;
	}

}
