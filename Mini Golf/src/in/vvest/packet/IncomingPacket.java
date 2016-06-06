package in.vvest.packet;

import java.awt.Color;
import java.nio.ByteBuffer;

import in.vvest.golf.Vec2;
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

public class IncomingPacket extends Packet {

	public IncomingPacket(byte[] data) {
		for (PacketType p : PacketType.values()) {
			if (p.getID() == data[0])
				this.type = p;
		}
		if (type == null)
			type = PacketType.INVALID;
		this.data = data;
		this.writtenData = data.length;
		readData = 1;
	}
	
	private Color decodeColor(int index) {
		int[] rgb = new int[3];
		for (int i = index; i < index + 3; i++) {
			int val = data[i];
			if (val < 0) val += 256;
			rgb[i - index] = val;
		}
		return new Color(rgb[0], rgb[1], rgb[2]);
	}
	
	private String decodeString(int index) {
		int end = index;
		for (end = index; end < data.length; end++) {
			if (data[end] == 0) break;
		}
		byte[] stringData = new byte[end - index];
		for (int i = index; i < end; i++) {
			stringData[i - index] = data[i];
		}
		return new String(stringData);
	}
	
	private double decodeDouble(int index) {
		ByteBuffer bb = ByteBuffer.wrap(data);
		return bb.getDouble(index);
	}
	
	private boolean decodeBoolean(int index) {
		return data[index] != 0;
	}
	
	private byte decodeByte(int index) {
		return data[index];
	}
	
	public Color nextColor() {
		Color res = decodeColor(readData);
		readData += 3;
		return res;
	}
	
	public String nextString() {
		String res = decodeString(readData);
		readData += res.getBytes().length;
		return res;
	}
	
	public double nextDouble() {
		double res = decodeDouble(readData);
		readData += 8;
		return res;
	}
	
	public boolean nextBoolean() {
		boolean res = decodeBoolean(readData);
		readData++;
		return res;
	}
	
	public byte nextByte() {
		byte res = decodeByte(readData);
		readData++;
		return res;
	}
	
	public Obstacle nextObstacle() {
		byte id = nextByte();
		if (id == ObstacleID.CIRCLE_HILL.getID()) {
			return new CircleHill(new Vec2(nextDouble(), nextDouble()), nextDouble(), (int) nextDouble(), (int) nextDouble(), nextDouble());	
		} else if (id == ObstacleID.CIRCLE_LINE.getID()) {
			return new CircleLine(new Vec2(nextDouble(), nextDouble()), nextDouble(), (int) nextDouble(), (int) nextDouble(), nextBoolean());
		} else if (id == ObstacleID.CIRCLE_WALL.getID()) {
			return new CircleWall(new Vec2(nextDouble(), nextDouble()), nextDouble(), (int) nextDouble(), (int) nextDouble(), nextDouble());
		} else if (id == ObstacleID.HOLE.getID()) {
			return new Hole(new Vec2(nextDouble(), nextDouble()), nextDouble());
		} else if (id == ObstacleID.LINE.getID()) {
			return new Line(new Vec2(nextDouble(), nextDouble()), new Vec2(nextDouble(), nextDouble()));
		} else if (id == ObstacleID.RECT_GRASS.getID()) {
			return new RectGrass(new Vec2(nextDouble(), nextDouble()), nextDouble(), nextDouble());
		} else if (id == ObstacleID.RECT_HILL.getID()) {
			return new RectHill(new Vec2(nextDouble(), nextDouble()), nextDouble(), nextDouble(), new Vec2(nextDouble(), nextDouble()));
		} else if (id == ObstacleID.WALL.getID()) {
			return new Wall(new Vec2(nextDouble(), nextDouble()), new Vec2(nextDouble(), nextDouble()), nextDouble());
		}
		return null;
	}
}
