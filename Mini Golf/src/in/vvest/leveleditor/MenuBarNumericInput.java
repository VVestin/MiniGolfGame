package in.vvest.leveleditor;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.Map;

import in.vvest.golf.Vec2;

public class MenuBarNumericInput extends MenuBarItem {

	private String input;
	
	public MenuBarNumericInput(int maxChars) {
		super(maxChars);
		input = "";
	}
	
	public void draw(Graphics g, Vec2 pos) {
		FontMetrics fm = g.getFontMetrics();
		g.setColor(Color.WHITE);
		g.drawRect((int) pos.x, (int) pos.y, getWidth(fm), fm.getHeight());
		g.drawString(input, (int) pos.x, (int) pos.y + fm.getHeight());
	}

	public void update(Map<String, Boolean> keyState) {
		
	}
}
