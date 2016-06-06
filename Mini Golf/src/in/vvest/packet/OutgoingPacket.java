package in.vvest.packet;

import java.awt.Color;
import java.nio.ByteBuffer;

import in.vvest.obstacles.CircleHill;
import in.vvest.obstacles.CircleLine;
import in.vvest.obstacles.CircleWall;
import in.vvest.obstacles.Hole;
import in.vvest.obstacles.Line;
import in.vvest.obstacles.Obstacle;
import in.vvest.obstacles.ObstacleID;
import in.vvest.obstacles.RectGrass;
import in.vvest.obstacles.RectHill;
import in.vvest.obstacles.Wall;
import in.vvest.server.PacketType;

public class OutgoingPacket extends Packet {

	public OutgoingPacket(PacketType type) {
		this.type = type;
		this.data = new byte[type.getSize()];
		this.data[0] = type.getID();
		this.writtenData = 1;
		
	}
	
	public void addData(byte[] newData) {
		for (int i = 0; i < newData.length; i++) {
			if (i >= data.length) {
				byte[] longerData = new byte[data.length + newData.length];
				for (int j = 0; j < data.length; j++) {
					longerData[j] = data[j];
				}
				data = longerData;
			}
			data[i + writtenData] = newData[i];
		}
		writtenData += newData.length;
	}
	
	public void addColor(Color c) {
		byte[] colorRGB = {(byte) c.getRed(), (byte) c.getGreen(), (byte) c.getBlue()};		
		addData(colorRGB);
	}
	
	public void addString(String s) {
		byte[] stringData = s.getBytes();
		byte[] data = new byte[stringData.length + 1];
		for (int i = 0; i < stringData.length; i++)
			data[i] = stringData[i];
		addData(s.getBytes());
	}
	
	public void addDouble(Double d) {
		ByteBuffer bb = ByteBuffer.allocate(Double.BYTES);
		bb.putDouble(d);
		addData(bb.array());
	}
	
	public void addBoolean(boolean b) {
		byte[] booleanData = {b ? (byte) 1 : (byte) 0};
		addData(booleanData);
	}
	
	public void addByte(byte b) {
		byte[] bytes = {b};
		addData(bytes);
	}
	
	public void addObstacle(Obstacle o) {
		if (o.getID() == ObstacleID.CIRCLE_HILL) {
			CircleHill ch = (CircleHill) o;
			addDouble(ch.getPos().x);
			addDouble(ch.getPos().y);
			addDouble(ch.getRadius());
			addDouble((double) ch.getStart());
			addDouble((double) ch.getEnd());
			addDouble((double) ch.getAcc());			
		} else if (o.getID() == ObstacleID.CIRCLE_LINE) {
			CircleLine cl = (CircleLine) o;
			addDouble(cl.getPos().x);
			addDouble(cl.getPos().y);
			addDouble(cl.getRadius());
			addDouble((double) cl.getStart());
			addDouble((double) cl.getEnd());
			addBoolean(cl.isFilled());
		} else if (o.getID() == ObstacleID.CIRCLE_WALL) {
			CircleWall cw = (CircleWall) o;
			addDouble(cw.getPos().x);
			addDouble(cw.getPos().y);
			addDouble(cw.getRadius());
			addDouble((double) cw.getStart());
			addDouble((double) cw.getEnd());
			addDouble(cw.getThickness());
		} else if (o.getID() == ObstacleID.HOLE) {
			Hole h = (Hole) o;
			addDouble(h.getPos().x);
			addDouble(h.getPos().y);
			addDouble(h.getRadius());
		} else if (o.getID() == ObstacleID.LINE) {
			Line l = (Line) o;
			addDouble(l.getA().x);
			addDouble(l.getA().y);
			addDouble(l.getB().x);
			addDouble(l.getB().y);
		} else if (o.getID() == ObstacleID.RECT_GRASS) {
			RectGrass rg = (RectGrass) o;
			addDouble(rg.getPos().x);
			addDouble(rg.getPos().y);
			addDouble(rg.getWidth());
			addDouble(rg.getHeight());
		} else if (o.getID() == ObstacleID.RECT_HILL) {
			RectHill rh = (RectHill) o;
			addDouble(rh.getPos().x);
			addDouble(rh.getPos().y);
			addDouble(rh.getWidth());
			addDouble(rh.getHeight());
			addDouble(rh.getAcc().x);
			addDouble(rh.getAcc().y);
		} else if (o.getID() == ObstacleID.WALL) {
			Wall w = (Wall) o;
			addDouble(w.getA().x);
			addDouble(w.getA().y);
			addDouble(w.getB().x);
			addDouble(w.getB().y);
			addDouble(w.getThickness());
		}
	}

	
}
