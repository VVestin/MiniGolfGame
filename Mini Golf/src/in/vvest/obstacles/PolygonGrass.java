package in.vvest.obstacles;

import in.vvest.golf.Ball;
import in.vvest.golf.Vec2;
import in.vvest.leveleditor.AdjustablePoint;
import in.vvest.obstacles.Obstacle;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.ArrayList;

public class PolygonGrass implements Obstacle {
	
    private Polygon area;

    public PolygonGrass(Polygon area) {
        this.area = area;
    }

    public  PolygonGrass(Vec2 ... points) {
        this.area = new Polygon();
        for (int i = 0; i < points.length; i++) {
            this.area.addPoint((int) points[i].x, (int) points[i].y);
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillPolygon(this.area);
    }

    public boolean resolveCollision(Ball b) {
        return false;
    }

	public ArrayList<AdjustablePoint> getAdjustmentPoints() {
		return null;
	}
    
    public ObstacleID getID() {
    	return null;
    }

	public Vec2 getPos() {
		return null;
	}
	
	public void setPos(Vec2 pos) {
		
	}
}