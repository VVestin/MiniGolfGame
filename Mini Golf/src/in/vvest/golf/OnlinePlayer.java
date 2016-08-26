package in.vvest.golf;

import java.awt.Color;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import in.vvest.game.Game;
import in.vvest.obstacles.Obstacle;
import in.vvest.packet.IncomingPacket;
import in.vvest.packet.OutgoingPacket;
import in.vvest.packet.Packet;
import in.vvest.server.PacketType;

public class OnlinePlayer extends Player implements Runnable {

	private DatagramSocket socket;
	private InetAddress serverAddress;
	private int serverPort;
	private Console console;
	private ArrayList<Player> players;
	private int count;
	private boolean connected;

	public OnlinePlayer(int numHoles, Color color, InetAddress serverAddress, int serverPort, DatagramSocket socket) {
		super(numHoles, color);
		players = new ArrayList<Player>();
		this.socket = socket;
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
		new Thread(this).start();
	}

	public void run() {
		while (console == null)	;
		while (true) {
			try {
				byte[] data = new byte[Packet.MAX_SIZE];
				DatagramPacket dataPacket = new DatagramPacket(data, data.length);
				socket.receive(dataPacket);
				IncomingPacket p = new IncomingPacket(data);
				if (p.getType() == PacketType.CONNECT) {
					Color playerColor = p.nextColor();
					if (!players.contains(playerColor)) {
						console.write(Game.colorString(playerColor), " has Connected");
						players.add(new Player(getScoreCard().length, playerColor));
					}	
					if (playerColor.equals(getColor()))
						connected = true;
				} else if (p.getType() == PacketType.PLAYER_LISTING) {
					for (int i = 0; i < Game.COLORS.length; i++) {
						
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void update(List<Obstacle> obstacles) {
		super.update(obstacles);
		if (count % 3 == 0) {
			OutgoingPacket packet = new OutgoingPacket(PacketType.UPDATE);			
			packet.addColor(getColor());
			packet.addByte((byte) getCurrentHole());
			packet.addDouble(getBall().getPos().x);
			packet.addDouble(getBall().getPos().y);
			packet.addDouble(getBall().getVel().x);
			packet.addDouble(getBall().getVel().y);
			packet.addDouble((double) getAngle());
			packet.addDouble(getPower());
			int[] scoreCard = getScoreCard();
			for (int i = 0; i < scoreCard.length; i++) {
				packet.addByte((byte) scoreCard[i]);
			}
			sendData(packet);
			
			if (!connected) {
				OutgoingPacket connectPacket = new OutgoingPacket(PacketType.CONNECT);
				connectPacket.addColor(getColor());
				sendData(connectPacket);
			}
		}
		count++;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public void setConsole(Console console) {
		this.console = console;
	}

	public void dispose() {
		OutgoingPacket p = new OutgoingPacket(PacketType.DISCONNECT);
		p.addColor(getColor());
		sendData(p);
	}
	
	public void processChat(String message) {
		if (message.startsWith("/")) {
			if (message.substring(1).equalsIgnoreCase("ping")) {
				sendData(new OutgoingPacket(PacketType.PING));
			} else if (message.startsWith("/kick ")) {
				Color c = Game.stringColor(message.substring(6));
				if (c != null) {
					OutgoingPacket p = new OutgoingPacket(PacketType.DISCONNECT);
					p.addColor(c);
					sendData(p);
				}
			}
		} else {
			OutgoingPacket packet = new OutgoingPacket(PacketType.MESSAGE);
			packet.addColor(getColor());
			packet.addString(message);
			sendData(packet);
		}
	}

	public void sendData(OutgoingPacket packet) {
		byte[] data = packet.getData();
		try {
			socket.send(new DatagramPacket(data, data.length, serverAddress, serverPort));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
