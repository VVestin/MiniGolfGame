package in.vvest.server;

import java.awt.Color;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ConnectedPlayer {

	private InetAddress address;
	private int port;
	private DatagramSocket socket;
	private Color color;
	
	public ConnectedPlayer(InetAddress address, int port, DatagramSocket socket, Color color) {
		this.address = address;
		this.port = port;
		this.socket = socket;
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void sendData(Packet packet) {
		byte[] data = packet.getData();
		try {
			socket.send(new DatagramPacket(data, data.length, address, port));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
