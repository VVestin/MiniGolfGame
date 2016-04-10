package in.vvest.golf;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

import in.vvest.game.Game;
import in.vvest.server.Packet;
import in.vvest.server.PacketType;

public class Console {

	public static final int MAX_MESSAGE_LENGTH = 85;

	private static final String VALID_CHARS = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789 .,?!@#$%^&*()-+_{[]}=\"':;<>/\\`~";
	private static final int MAX_SIZE = 40;
	
	private boolean open, t;
	private ArrayDeque<String> console;
	private String currentEntry, lastEntry;
	private OnlinePlayer player;
	
	public Console(OnlinePlayer player) {
		open = false;
		console = new ArrayDeque<String>();
		this.player = player;
	}
	
	public void draw(Graphics g) {
		g.setFont(Game.XTRA_SMALL_FONT);
		FontMetrics fm = g.getFontMetrics();
		int height = fm.getHeight();
		if (open) {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, 150, 400);
			g.setColor(Color.WHITE);
			g.drawLine(150, 0, 150, 400 - height);
			g.drawLine(0, 400 - height, 400, 400 - height);
			int line = 1;
			Iterator<String> i = console.descendingIterator();
			try {
				while (i.hasNext()) {
					String next = i.next();
					line = printMessage(g, fm, height, next, line);
				}
			} catch (ConcurrentModificationException e) {}
			g.drawString(currentEntry, 5, 398);
			g.translate(150, -height);
		} else if (!console.isEmpty()) {
			printMessage(g, fm, height, console.peekLast(), 0);
		}
	}
	
	private int printMessage(Graphics g, FontMetrics fm, int height, String message, int line) {
		String[] characters = message.split("");
		ArrayList<String> lines = new ArrayList<String>();
		int xOffset = 5;
		int startLine = line;
		String currentLine = "";
		for (int j = 0; j < characters.length; j++) {
			if (xOffset > 150) {
				xOffset = 5;
				lines.add(currentLine);
				currentLine = "";
			}
			currentLine += characters[j];
			xOffset += fm.stringWidth(characters[j]);
		}
		if (currentLine.length() > 0) lines.add(currentLine);
		for (int j = lines.size() - 1; j >= 0; j--) {
			printLine(g, fm, height, lines.get(j), 5, startLine + (lines.size() - j));
		}
		return line + lines.size();
	}
	
	private void printLine(Graphics g, FontMetrics fm, int height, String message, int xOffset, int line) {
		int index = 0;
		while (index < message.length()) { 
			int closestColorIndex = message.length();
			String closestColorName = "";
			for (int i = 0; i < Game.COLOR_NAMES.length; i++) {
				int indexOfColor = message.indexOf(Game.COLOR_NAMES[i], index);
				if (indexOfColor != -1 && indexOfColor < closestColorIndex) {
					closestColorIndex = indexOfColor;
					closestColorName = Game.COLOR_NAMES[i];
				}
			}
			if (closestColorIndex != -1) {
				g.setColor(Color.WHITE);
				g.drawString(message.substring(index, closestColorIndex), xOffset + fm.stringWidth(message.substring(0, index)), 400 - line * height);
				g.setColor(Game.stringColor(closestColorName));
				g.drawString(closestColorName, xOffset + fm.stringWidth(message.substring(0, closestColorIndex)), 400 - line * height);
				index = closestColorIndex + closestColorName.length();
			} else {
				g.setColor(Color.WHITE);
				g.drawString(message.substring(index), xOffset + fm.stringWidth(message.substring(0, index)), 400 - line * height);
				break;
			}
		}
	}
	
	public boolean isOpen() {
		return open;
	}
	
	public void write(String sender, String msg) {
		if (msg.length() > 0) {
			if (sender.equals("RED"))
				sender = "RED ";
			console.add(sender + ": " + msg);
			if (console.size() > MAX_SIZE) console.remove();
		}
	}
	
	public void keyPressed(KeyEvent e) {
		int k = e.getKeyCode();
		if (k == KeyEvent.VK_T && !open && !t) {
			open = true;
			currentEntry = "";
			t = true;
		} else if (k == KeyEvent.VK_ENTER && open && currentEntry.length() > 0) {
			if (currentEntry.startsWith("/")) {
				if (currentEntry.substring(1).equalsIgnoreCase("ping")) {
					player.sendData(PacketType.PING.createPacket());
				}
			} else {
				Packet packet = PacketType.MESSAGE.createPacket();
				packet.addColor(player.getColor());
				packet.addString(currentEntry);
				player.sendData(packet);
			}
			lastEntry = currentEntry;
			currentEntry = "";
		} else if (k == KeyEvent.VK_ESCAPE && open) {
			open = false;
		} else if (k == KeyEvent.VK_BACK_SPACE && open && currentEntry.length() > 0) {
			currentEntry = currentEntry.substring(0, currentEntry.length() - 1);
		} else if (k == KeyEvent.VK_UP && open && lastEntry != null) {
			currentEntry = lastEntry;
		} else {
			char c = e.getKeyChar();
			if (currentEntry != null && VALID_CHARS.indexOf(c) != -1 && currentEntry.length() < MAX_MESSAGE_LENGTH) {
				currentEntry += c;
			}
		}
	}
	
	public void keyReleased(KeyEvent e) {
		int k = e.getKeyCode();
		if (k == KeyEvent.VK_T) {
			t = false;
		}
	}
	
}
