package in.vvest.obstacles;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Map;

import in.vvest.golf.Ball;
import in.vvest.golf.Vec2;
import in.vvest.leveleditor.AbstractAdjustablePoint;
import in.vvest.leveleditor.AdjustablePoint;
import in.vvest.leveleditor.TranslationPoint;

public class RectWater implements Obstacle {
	private Vec2 pos;
	private double width, height;
	
	public RectWater(Vec2 pos, double width, double height) {
		this.pos = pos;
		this.width = width;
		this.height = height;
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.BLUE.brighter());
		g.fillRect((int) pos.x, (int) pos.y, (int) width, (int) height); 
	}

	public boolean resolveCollision(Ball b) {
		if (b.getPos().x > pos.x && b.getPos().x < pos.x + width
				&& b.getPos().y > pos.y && b.getPos().y < pos.y + height) {
			b.setPos(b.getLastHitPos());
			b.setVel(new Vec2(0, 0));
			return true;
		}
		return false;
	}
	
	public Vec2 getPos() {
		return pos;
	}
	
	public double getWidth() {
		return width;
	}
	
	public double getHeight() {
		return height;
	}

	public ArrayList<AdjustablePoint> getAdjustmentPoints() {
		ArrayList<AdjustablePoint> points = new ArrayList<AdjustablePoint>();
		points.add(new TranslationPoint());
		points.add(new AbstractAdjustablePoint() {
			public void update(Obstacle o, Map<String, Boolean> keyState) {
				if (keyState.containsKey("a") && keyState.get("a"))
					width--;
				if (keyState.containsKey("d") && keyState.get("d"))
					width++;
				if (keyState.containsKey("w") && keyState.get("w"))
					height--;
				if (keyState.containsKey("s") && keyState.get("s"))
					height++;
			}
			
			protected Vec2 getPos(Obstacle o) {
				return new Vec2(pos.x + width, pos.y + height);
			}
			
		});
		return points;
	}
    
    public ObstacleID getID() {
    	return ObstacleID.RECT_GRASS;
    }

	public void setPos(Vec2 pos) {
		this.pos = pos;
	}
}
