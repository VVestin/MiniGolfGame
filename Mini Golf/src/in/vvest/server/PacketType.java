package in.vvest.server;

import in.vvest.golf.Console;

public enum PacketType {

	INVALID(-1, 1), 
	PING(0, 1), 
	PONG(1, 1), 
	CONNECT(2, 5), 
	DISCONNECT(3, 5), 
	MESSAGE(4, 4 + Console.MAX_MESSAGE_LENGTH), 
	COLOR_IN_USE(5, 5), 
	UPDATE(6, 256),
	OBSTACLE(7, 34);

	private byte id;
	private int size;

	private PacketType(int id, int size) {
		this.id = (byte) id;
		this.size = size;
	}

	public Packet createPacket() {
		return new Packet(this, new byte[size]);
	}

	public byte getID() {
		return id;
	}

}
