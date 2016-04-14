package in.vvest.leveleditor;

import java.util.Map;

import in.vvest.golf.Vec2;
import in.vvest.obstacles.Obstacle;

public class TranslationPoint extends AbstractAdjustablePoint {

	public void update(Obstacle o, Map<String, Boolean> keyState) {
		Vec2 pos = o.getPos();
		if (keyState.containsKey("a") && keyState.get("a"))
			pos = pos.add(new Vec2(-1, 0));
		if (keyState.containsKey("d") && keyState.get("d"))
			pos = pos.add(new Vec2(1, 0));
		if (keyState.containsKey("w") && keyState.get("w"))
			pos = pos.add(new Vec2(0, -1));
		if (keyState.containsKey("s") && keyState.get("s"))
			pos = pos.add(new Vec2(0, 1));
		o.setPos(pos);
	}

	protected Vec2 getPos(Obstacle o) {
		return o.getPos();
	}
	
}
