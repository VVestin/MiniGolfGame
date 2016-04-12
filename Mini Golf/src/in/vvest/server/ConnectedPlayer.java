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
	private long lastUpdate;
	
	public ConnectedPlayer(InetAddress address, int port, DatagramSocket socket, Color color) {
		this.address = address;
		this.port = port;
		this.socket = socket;
		this.color = color;
		lastUpdate = System.currentTimeMillis();
	}


	public void setLastUpdate(long lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	
	public long getLastUpdate() {
		return lastUpdate;
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
	
	public void sendData(Packet packet, int delay) {
		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				byte[] data = packet.getData();
				try {
					socket.send(new DatagramPacket(data, data.length, address, port));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}).start();	
	}

	public boolean equals(ConnectedPlayer p) {
		return p.getColor().equals(color);
	}	
}
