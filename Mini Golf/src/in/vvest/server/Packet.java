package in.vvest.server;

import java.awt.Color;
import java.nio.ByteBuffer;

public class Packet {

	private PacketType type;
	private byte[] data;
	private int writtenData;
	private int readData;

	public Packet(byte[] data) {
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

	public Packet(PacketType type, byte[] data) {
		this.type = type;
		this.data = new byte[data.length + 1];
		this.data[0] = type.getID();
		for (int i = 1; i < this.data.length; i++) {
			this.data[i] = data[i - 1];
		}
		this.writtenData = 1;
		readData = 1;
	}
	
	public PacketType getType() {
		return type;
	}

	public byte[] getData() {
		return data;
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
}
