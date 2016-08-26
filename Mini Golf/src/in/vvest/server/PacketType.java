package in.vvest.server;

import in.vvest.game.Game;
import in.vvest.golf.Console;

public enum PacketType {

	/*************************\
	* PacketTypes(id, size)   *
	* NUM_BYTES - USE;        *
	\*************************/
	
	// 1 - packetID;
	INVALID(-1, 1),
	
	// 1 - packetID; 
	PING(0, 1),
	
	// 1 - packetID;
	PONG(1, 1),
	
	// 1 - packetID; 3 - Color;
	CONNECT(2, 5),
	
	// 1 - packetID; 3 - Color;
	//CONNECT_ACK(3, 5),
	
	// 1 - packetID; 3 - Color;
	DISCONNECT(4, 5),
	
	// 1 - packetID; 3 - Color; MAX_MESSAGE_LENGTH - Message
	MESSAGE(5, 4 + Console.MAX_MESSAGE_LENGTH),
	
	// 1 - packetID; 3 - Color; 1 - available
	COLOR_IN_USE(6, 5),
	
	// 1 - packetID; 3 - Color; 1 - currentHole;
	// 8 - ballX; 8 - ballY; 8 - ballVelX; 8 - ballVelY;
	// 8 - angle; 8 - power; 18 - scoreCard;
	UPDATE(7, 256),
	
	// 1 - packetID; The rest depends on the obstacle
	OBSTACLE(8, 34),
	
	// 1 - packetID; COLORS.length * 3 - UsedColors
	PLAYER_LISTING(9, Game.COLORS.length * 3 + 1);

	private byte id;
	private int size;

	private PacketType(int id, int size) {
		this.id = (byte) id;
		this.size = size;
	}

	public byte getID() {
		return id;
	}
	
	public int getSize() {
		return size;
	}

}
