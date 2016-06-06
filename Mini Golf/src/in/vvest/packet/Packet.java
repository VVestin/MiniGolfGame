package in.vvest.packet;

import in.vvest.server.PacketType;

public abstract class Packet {
	public static final int MAX_SIZE = 128;

	protected PacketType type;
	protected byte[] data;
	protected int writtenData;
	protected int readData;
	
	public PacketType getType() {
		return type;
	}

	public byte[] getData() {
		return data;
	}
	
}
