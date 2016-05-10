package in.vvest.leveleditor;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.Map;

import in.vvest.golf.Vec2;

public abstract class MenuBarItem {

	private int charWidth;
	
	public MenuBarItem(int charWidth) {
		this.charWidth = charWidth;
	}
	
	public abstract void draw(Graphics g, Vec2 pos);
	
	public void update(Map<String, Boolean> keyState) {};

	public int getWidth(FontMetrics fm) {
		return charWidth * fm.charWidth('W');
	}
}
