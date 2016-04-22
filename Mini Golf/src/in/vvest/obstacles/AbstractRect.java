package in.vvest.obstacles;

import java.util.ArrayList;
import java.util.Map;

import in.vvest.golf.Vec2;
import in.vvest.leveleditor.AbstractAdjustablePoint;
import in.vvest.leveleditor.AdjustablePoint;
import in.vvest.leveleditor.TranslationPoint;

public abstract class AbstractRect implements Obstacle {

	private Vec2 pos;
	private double width, height;
	
	public AbstractRect(Vec2 pos, double width, double height) {
		this.pos = pos;
		this.width = width;
		this.height = height;
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
	
	public Vec2 getPos() {
		return pos;
	}
	
	public double getWidth() {
		return width;
	}
	
	public double getHeight() {
		return height;
	}	
	
	public void setPos(Vec2 pos) {
		this.pos = pos;
	}
}
