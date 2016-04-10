package in.vvest.golf;

import java.util.ArrayList;

public class GolfHole {

	private ArrayList<Obstacle> obstacles;
	private Vec2 startPos;
	private int par;
	
	public GolfHole(ArrayList<Obstacle> obstacles, Vec2 startPos, int par) {
		this.obstacles = obstacles;
		this.startPos = startPos;
		this.par = par;
	}

	public ArrayList<Obstacle> getObstacles() {
		return obstacles;
	}

	public Vec2 getStartPos() {
		return startPos;
	}

	public int getPar() {
		return par;
	}
}
