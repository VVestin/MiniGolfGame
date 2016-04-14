package in.vvest.golf;

import java.awt.Color;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import in.vvest.game.Game;
import in.vvest.obstacles.Obstacle;
import in.vvest.server.Packet;
import in.vvest.server.PacketType;

public class OnlinePlayer extends Player implements Runnable {
	
	private DatagramSocket socket;
	private InetAddress serverAddress;
	private Console console;
	private ArrayList<Player> players;
	private int serverPort;
	
	public OnlinePlayer(int numHoles, Color color, InetAddress serverAddress, int serverPort) {
		super(numHoles, color);
		players = new ArrayList<Player>();
		try {
			socket = new DatagramSocket();
			this.serverAddress = serverAddress;
			this.serverPort = serverPort;
			new Thread(this).start();
			Packet packet = PacketType.CONNECT.createPacket();
			packet.addColor(color);
			sendData(packet);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		while (console == null);
		while (true) {
			try {
				byte[] data = new byte[Packet.MAX_SIZE];
				DatagramPacket dataPacket = new DatagramPacket(data, data.length);
				socket.receive(dataPacket);
				Packet p = new Packet(data);
				// TODO make code more robust for errors the server could have
				if (p.getType() == PacketType.PONG) {
					console.write("SRVR","PONG");
				} else if (p.getType() == PacketType.CONNECT) {
					Color c = p.nextColor();
					players.add(new Player(getScoreCard().length, c));
					console.write("SRV", Game.colorString(c) + " has connected");
				} else if (p.getType() == PacketType.DISCONNECT) {
					Color c = p.nextColor();
					if (c.equals(getColor())) {
						System.exit(0);
					}
					for (int i = 0; i < players.size(); i++) {
						if (players.get(i).getColor().equals(c)) {
							players.remove(i);
							break;
						}							
					}
					console.write("SRV", Game.colorString(c) + " has disconnected");
				} else if (p.getType() == PacketType.MESSAGE) {
					Color sender = p.nextColor();
					String msg = p.nextString();
					console.write(Game.colorString(sender), msg);
				} else if (p.getType() == PacketType.UPDATE) {
					Color c = p.nextColor();
					Player player = null;
					for (int i = 0; i < players.size(); i++) {
						if (players.get(i).getColor().equals(c)) {
							player = players.get(i);
							break;
						}
					}
					if (player == null) {
						player = new Player(getScoreCard().length, c);
						players.add(player);
					}
					player.setCurrentHole(p.nextByte());
					double x = p.nextDouble();
					double y = p.nextDouble();
					double velX = p.nextDouble();
					double velY = p.nextDouble();
					int angle = (int) p.nextDouble();
					double power = p.nextDouble();
					player.getBall().setPos(new Vec2(x, y));
					player.getBall().setVel(new Vec2(velX, velY));
					player.setAngle(angle);
					player.setPower(power);
					int[] scoreCard = new int[player.getScoreCard().length];
					for (int i = 0; i < scoreCard.length; i++) {
						scoreCard[i] = p.nextByte();
					}
					player.setScoreCard(scoreCard);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void update(List<Obstacle> obstacles) {
		super.update(obstacles);
		Packet packet = PacketType.UPDATE.createPacket();
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
	}
	
	public ArrayList<Player> getPlayers() {
		return players;
	}
	
	public void setConsole(Console console) {
		this.console = console;
	}
	
	public void dispose() {
		Packet p = PacketType.DISCONNECT.createPacket();
		p.addColor(getColor());
		sendData(p);
	}
	
	public void sendData(Packet packet) {
		byte[] data = packet.getData();
		try {
			socket.send(new DatagramPacket(data, data.length, serverAddress, serverPort));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
}
