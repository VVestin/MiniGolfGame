package in.vvest.server;

import java.awt.Color;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import in.vvest.packet.IncomingPacket;
import in.vvest.packet.OutgoingPacket;
import in.vvest.packet.Packet;

public class Server extends Thread {

	public static final int PORT = 9091;
	public static final InetAddress ADDRESS = getAddress();

	private DatagramSocket socket;
	private List<ConnectedPlayer> clients;
	private long lastOfflineCheck;

	public Server() {
		try {
			socket = new DatagramSocket(PORT);
		} catch (SocketException e) {
			e.printStackTrace();
		}

		clients = new ArrayList<ConnectedPlayer>();
	}

	public void run() {
		System.out.println("Starting Server");
		while (true) {
			try {
				byte[] data = new byte[Packet.MAX_SIZE];
				DatagramPacket dataPacket = new DatagramPacket(data, data.length);
				socket.receive(dataPacket);
				IncomingPacket incomingPacket = new IncomingPacket(data);
				if (incomingPacket.getType() == PacketType.PING) {
					System.out.println("Ping!");
					sendData(new OutgoingPacket(PacketType.PONG), dataPacket.getAddress(), dataPacket.getPort());
				} else if (incomingPacket.getType() == PacketType.PONG) {
					System.out.println("Pong!");
				} else if (incomingPacket.getType() == PacketType.COLOR_IN_USE) {
					Color playerColor = incomingPacket.nextColor();
					OutgoingPacket packet = new OutgoingPacket(PacketType.COLOR_IN_USE);
					packet.addColor(playerColor);
					boolean available = isConnected(playerColor);
					packet.addBoolean(available);
					sendData(packet, dataPacket.getAddress(), dataPacket.getPort());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private boolean isConnected(Color c) {
		for (int i = 0; i < clients.size(); i++) {
			if (clients.get(i).getColor().equals(c)) {
				return true;
			}
		}
		return false;
	}
	
	private void sendData(OutgoingPacket packet, InetAddress address, int port) {
		byte[] data = packet.getData();
		try {
			socket.send(new DatagramPacket(data, data.length, address, port));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static InetAddress getAddress() {
		try {
			return InetAddress.getByName("67.177.226.182");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) throws Exception {
		Server s = new Server();
		s.start();
	}

}
