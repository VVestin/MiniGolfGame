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

public class Server extends Thread {

	public static final int PORT = 9091;
	public static final InetAddress ADDRESS = getAddress();

	private DatagramSocket socket;
	private List<ConnectedPlayer> clients;

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
				byte[] data = new byte[256];
				DatagramPacket dataPacket = new DatagramPacket(data, data.length);
				socket.receive(dataPacket);
				Packet p = new Packet(data);
				if (p.getType() == PacketType.PING) {
					System.out.println("Ping!");
					sendData(PacketType.PONG.createPacket(), dataPacket.getAddress(), dataPacket.getPort());
				} else if (p.getType() == PacketType.CONNECT) {
					System.out.println("Connection!");
					clients.add(new ConnectedPlayer(dataPacket.getAddress(), dataPacket.getPort(), socket, p.decodeColor(1)));
					System.out.println(clients.size() + " players connnected");
					Packet packet = PacketType.CONNECT.createPacket();
					packet.addColor(p.decodeColor(1));
					byte[] numConnected = {(byte) (clients.size())};
					packet.addData(numConnected);
					for (int i = 0; i < clients.size(); i++) {
						clients.get(i).sendData(packet);
					}
				} else if (p.getType() == PacketType.DISCONNECT) {
					System.out.println("Disconnection!");
					Color color = p.decodeColor(1);
					p = PacketType.DISCONNECT.createPacket();
					p.addColor(color);
					byte[] numConnected = {(byte) (clients.size() - 1)};
					p.addData(numConnected);
					for (int i = clients.size() - 1; i >= 0; i--) {
						if (clients.get(i).getColor().equals(color)) {
							clients.remove(i);
						} else {
							clients.get(i).sendData(p);
						}
					}
				} else if (p.getType() == PacketType.MESSAGE) {
					for (int i = 0; i < clients.size(); i++) {
						clients.get(i).sendData(p);
					}
				} else if (p.getType() == PacketType.COLOR_IN_USE) {
					Color c = p.decodeColor(1);
					boolean available = true;
					for (int i = 0; i < clients.size(); i++) {
						if (clients.get(i).getColor().equals(c)) {
							available = false;
							break;
						}
					}
					Packet packet = PacketType.COLOR_IN_USE.createPacket();
					packet.addColor(c);
					packet.addBoolean(available);
					sendData(packet, dataPacket.getAddress(), dataPacket.getPort());
				} else if (p.getType() == PacketType.UPDATE) {
					Color c = p.decodeColor(1);
					for (int i = 0; i < clients.size(); i++) {
						if (!clients.get(i).getColor().equals(c))
							clients.get(i).sendData(p);
					}					
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void sendData(Packet packet, InetAddress address, int port) {
		byte[] data = packet.getData();
		try {
			socket.send(new DatagramPacket(data, data.length, address, port));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static InetAddress getAddress() {
		try {
			return InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) throws Exception {
		Server s = new Server();
		s.start();
		Client c = new Client();
		c.sendData(PacketType.PING.createPacket(), Server.ADDRESS, Server.PORT);
	}

}
