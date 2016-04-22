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

import in.vvest.game.Game;

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
				Packet p = new Packet(data);
				if (p.getType() == PacketType.PING) {
					System.out.println("Ping!");
					sendData(PacketType.PONG.createPacket(), dataPacket.getAddress(), dataPacket.getPort());
				} else if (p.getType() == PacketType.CONNECT) {
					Color c = p.nextColor();
					if (!isConnected(c)) {
						System.out.println("Connection!");
						clients.add(new ConnectedPlayer(dataPacket.getAddress(), dataPacket.getPort(), socket, c));
						for (int i = 0; i < clients.size(); i++) {
							clients.get(i).sendData(p);
						}
					}
				} else if (p.getType() == PacketType.DISCONNECT) {
					Color c = p.nextColor();
					if (isConnected(c)) {				
						System.out.println("Disconnection!");
						for (int i = clients.size() - 1; i >= 0; i--) {
							if (clients.get(i).getColor().equals(c)) {
								clients.get(i).sendData(p);
								clients.remove(i);
							} else {
								System.out.println("Telling " + Game.colorString(clients.get(i).getColor()));
								clients.get(i).sendData(p, 500);
							}
						}
					}
				} else if (p.getType() == PacketType.MESSAGE) {
					Color c = p.nextColor();
					if (isConnected(c)) {
						for (int i = 0; i < clients.size(); i++) {
							clients.get(i).sendData(p);
						}
					}
				} else if (p.getType() == PacketType.COLOR_IN_USE) {
					Color c = p.nextColor();
					Packet packet = PacketType.COLOR_IN_USE.createPacket();
					packet.addColor(c);
					packet.addBoolean(!isConnected(c));
					sendData(packet, dataPacket.getAddress(), dataPacket.getPort());
				} else if (p.getType() == PacketType.UPDATE) {
					Color c = p.nextColor();
					if (isConnected(c)) {
						for (int i = 0; i < clients.size(); i++) {
							if (clients.get(i).getColor().equals(c))
								clients.get(i).setLastUpdate(System.currentTimeMillis());
							else 								
								clients.get(i).sendData(p);
						}					
					}
				}

				if (System.currentTimeMillis() - lastOfflineCheck > 10_000) {	
					for (int i = clients.size() - 1; i >= 0; i--) {
						if (System.currentTimeMillis() - clients.get(i).getLastUpdate() > 5_000) {				
							System.out.println("Disconnection!");
							Packet packet = PacketType.DISCONNECT.createPacket();
							packet.addColor(clients.get(i).getColor());
							packet.addByte((byte) (clients.size() - 1)); 
							for (int j = 0; j < clients.size(); j++) {
								clients.get(j).sendData(packet);
							}
							clients.remove(i);
						}
					}
					lastOfflineCheck = System.currentTimeMillis();
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
			return InetAddress.getByName("67.177.226.182");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) throws Exception {
		Server s = new Server();
		s.start();
		//Client c = new Client();
		//c.sendData(PacketType.PING.createPacket(), Server.ADDRESS, Server.PORT);
	}

}
