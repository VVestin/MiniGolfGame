package in.vvest.golf;

import in.vvest.golf.Ball;
import in.vvest.golf.Obstacle;
import in.vvest.golf.Vec2;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

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
    
    public ObstacleID getID() {
    	return null;
    }
}