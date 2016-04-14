package in.vvest.gamestates;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import in.vvest.game.Game;
import in.vvest.game.GameStateManager;
import in.vvest.server.Packet;
import in.vvest.server.PacketType;
import in.vvest.server.Server;

public class MainMenuState extends GameState implements Runnable {

	private int selectedColor;
	private boolean[] colorAvailable;
	private Color confirmedColor;
	private DatagramSocket socket;
	private InetAddress address;
	private int port;
	private long lastAvailabilityCheck;

	public MainMenuState(GameStateManager gsm) {
		super(gsm);
		address = Server.ADDRESS;
		port = Server.PORT;
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		new Thread(this).start();
		colorAvailable = new boolean[Game.COLORS.length];
	}

	public void update() {
		if (System.currentTimeMillis() - lastAvailabilityCheck > 100) {
			for (int i = 0; i < Game.COLORS.length; i++) {
				Packet p = PacketType.COLOR_IN_USE.createPacket();
				p.addColor(Game.COLORS[i]);
				sendData(p);
			}
			lastAvailabilityCheck = System.currentTimeMillis();
		}
	}

	public void run() {
		while (true) {
			try {
				byte[] data = new byte[5];
				DatagramPacket dataPacket = new DatagramPacket(data, data.length);
				socket.receive(dataPacket);
				Packet p = new Packet(data);
				if (p.getType() == PacketType.COLOR_IN_USE) {
					Color c = p.nextColor();
					for (int i = 0; i < Game.COLORS.length; i++) {
						if (c.equals(Game.COLORS[i])) {
							colorAvailable[i] = p.nextBoolean();
							if (confirmedColor != null && confirmedColor.equals(c) && colorAvailable[i])
									gsm.setGameState(gsm.getCurrentGameState(), new PlayState(gsm, confirmedColor));
							break;
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void draw(Graphics g) {
		g.setFont(Game.LARGE_FONT);
		g.setColor(Color.WHITE);
		FontMetrics fm = g.getFontMetrics();
		g.drawString("Mini Golf", (400 - fm.stringWidth("Mini Golf")) / 2, 100);
		g.setColor(Color.GREEN);
		g.fillRect(30, 150, 340, 80);
		int cellWidth = 35;
		int width = cellWidth * (Game.COLORS.length);
		int xOffset = 200 - width / 2;
		for (int i = 0; i < Game.COLORS.length; i++) {
			g.setColor(Color.BLACK);
			if (i == selectedColor)
				g.drawRect(xOffset + cellWidth * i - 5, 190 - 10, 20, 20);
			if (colorAvailable[i])
				g.setColor(Game.COLORS[i]);
			else
				g.setColor(Color.BLACK);
			g.fillOval(xOffset + cellWidth * i, 190 - 5, 10, 10);
			g.setColor(Color.BLACK);
			g.drawOval(xOffset + cellWidth * i, 190 - 5, 10, 10);
		}
	}

	public void keyPressed(KeyEvent e) {
		int k = e.getKeyCode();
		if (k == KeyEvent.VK_ENTER || k == KeyEvent.VK_SPACE && confirmedColor == null) {
			confirmedColor = Game.COLORS[selectedColor];
			Packet packet = PacketType.COLOR_IN_USE.createPacket();
			packet.addColor(confirmedColor);
			sendData(packet);
		}
		if ((k == KeyEvent.VK_A || k == KeyEvent.VK_LEFT) && selectedColor > 0)
			selectedColor--;
		if ((k == KeyEvent.VK_D || k == KeyEvent.VK_RIGHT) && selectedColor < Game.COLORS.length - 1)
			selectedColor++;
	}

	private void sendData(Packet packet) {
		byte[] data = packet.getData();
		try {
			socket.send(new DatagramPacket(data, data.length, address, port));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
