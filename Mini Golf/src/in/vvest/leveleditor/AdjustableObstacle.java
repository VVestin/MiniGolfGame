package in.vvest.leveleditor;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Map;

import in.vvest.obstacles.Obstacle;

public class AdjustableObstacle {

	private Obstacle obstacle;
	private ArrayList<AdjustablePoint> adjustmentPoints;
	private int currentPoint;
	
	public AdjustableObstacle(Obstacle obstacle) {
		this.obstacle = obstacle;
		adjustmentPoints = obstacle.getAdjustmentPoints();
		currentPoint = 1;
	}
	
	public void draw(Graphics g) {
		obstacle.draw(g);
		for (int i = 0; i < adjustmentPoints.size(); i++) {
			adjustmentPoints.get(i).draw(g, obstacle, i == currentPoint);
		}
	}
	
	public void update(Map<String, Boolean> keyState) {
		adjustmentPoints.get(currentPoint).update(obstacle, keyState);
		if (keyState.containsKey("tab") && keyState.get("tab")) {
			currentPoint++;
			if (currentPoint >= adjustmentPoints.size())
				currentPoint = 0;
		} else if (keyState.containsKey("shift") && keyState.get("shift")) {
			currentPoint--;
			if (currentPoint <= 0)
				currentPoint = adjustmentPoints.size() - 1;
		}
	}
	
	public Obstacle getObstacle() {
		return obstacle;
	}	
}
