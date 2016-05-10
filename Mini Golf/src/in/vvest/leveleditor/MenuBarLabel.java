package in.vvest.leveleditor;

import java.awt.Color;
import java.awt.Graphics;

import in.vvest.golf.Vec2;

public class MenuBarLabel extends MenuBarItem {

	private String label;
	
	public MenuBarLabel(String label) {
		super(label.length());
	}

	public void draw(Graphics g, Vec2 pos) {
		g.setColor(Color.WHITE);
		g.drawString(label, (int) pos.x, (int) pos.y + g.getFontMetrics().getHeight());
	}
}
