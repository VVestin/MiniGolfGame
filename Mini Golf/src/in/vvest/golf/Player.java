package in.vvest.golf;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.List;

import in.vvest.obstacles.Obstacle;

public class Player {
	
	private Ball b;
	private Color color;
	private double power;
	private boolean up, down, left, right;
	private int strokes, currentHole, angle, dir;
	private int[] scoreCard;

	public Player(int numHoles, Color color) {
		scoreCard = new int[numHoles];
		this.color = color;
		b = new Ball(new Vec2(50, 50), 4);
		power = 30;
		angle = -90;
		dir = 1;
		currentHole = -1;
	}

	public void draw(Graphics g) {
		if (b.getVel().lengthSquared() == 0) {
			g.setColor(Color.BLACK);
			Vec2 target = b.getPos().add(new Vec2(Math.toRadians(angle)).scale(power));
			g.drawLine((int) b.getPos().x, (int) b.getPos().y, (int) target.x, (int) target.y);
		}
		b.draw(g, color);
		g.setColor(Color.WHITE);
		g.drawString("" + (360 - angle), 5, 17);
		g.drawString("" + power, 5, 25);
	}

	public void update(List<Obstacle> obstacles) {			
		b.update(obstacles);
		if (b.isInHole())
			scoreCard[currentHole] = strokes;
		if (up && power < 60)
			power += .5;
		if (down && power > .5)
			power -= .5;
		if (left)
			angle -= dir;
		if (right)
			angle += dir;
		angle = angle % 360;
		if (angle < 0)
			angle += 360;
	}
	
	public Ball getBall() {
		return b;
	}
	
	public int getStrokes() {
		return strokes;
	}
	
	public boolean isInHole() {
		return b.isInHole();
	}
	
	public int getAngle() {
		return angle;
	}
	
	public double getPower() {
		return power;
	}
	
	public int[] getScoreCard() {
		return scoreCard;
	}
	
	public int getCurrentHole() {
		return currentHole;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setAngle(int angle) {
		this.angle = angle;
	}
	
	public void setPower(double power) {
		this.power = power;
	}

	public void setScoreCard(int[] scoreCard) {
		this.scoreCard = scoreCard;
	}
	
	public void setCurrentHole(int hole) {
		strokes = 0;
		currentHole = hole;
	}
	
	public void setStrokes(int strokes) {
		this.strokes = strokes;
	}
	
	public void keyPressed(int k) {
		if (k == KeyEvent.VK_SPACE) {
			if (power != 0 && b.getVel().lengthSquared() == 0) {
				b.setVel(new Vec2(Math.toRadians(angle)).scale(power * power * .008));
				strokes++;
			}
		} else if ((k == KeyEvent.VK_UP || k == KeyEvent.VK_W) && power > 0) {
			up = true;
		} else if ((k == KeyEvent.VK_DOWN || k == KeyEvent.VK_S) && power > 0) {
			down = true;
		} else if ((k == KeyEvent.VK_LEFT || k == KeyEvent.VK_A) && power > 0) {
			left = true;
		} else if ((k == KeyEvent.VK_RIGHT || k == KeyEvent.VK_D) && power > 0) {
			right = true;
		}
	}

	public void keyReleased(int k) {
		if ((k == KeyEvent.VK_UP || k == KeyEvent.VK_W) && power > 0) {
			up = false;
		} else if ((k == KeyEvent.VK_DOWN || k == KeyEvent.VK_S) && power > 0) {
			down = false;
		} else if ((k == KeyEvent.VK_LEFT || k == KeyEvent.VK_A) && power > 0) {
			left = false;
			if (Math.abs(angle) > 180)
				dir = 1;
			else
				dir = -1;
		} else if ((k == KeyEvent.VK_RIGHT || k == KeyEvent.VK_D) && power > 0) {
			right = false;
			if (Math.abs(angle) > 180)
				dir = 1;
			else
				dir = -1;
		}
	}
}
