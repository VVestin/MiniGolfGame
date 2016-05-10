package in.vvest.leveleditor;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import in.vvest.game.Game;
import in.vvest.golf.Vec2;

public class MenuBar {
	private static final int ITEM_MARGIN = 10;
	
	private List<MenuBarItem> items;
	private double yPos;
	
	public MenuBar(double yPos) {
		this.yPos = yPos;
		items = new ArrayList<MenuBarItem>();
	}
	
	public void draw(Graphics g) {
		g.setFont(Game.SMALL_FONT);
		FontMetrics fm = g.getFontMetrics();
		g.setColor(Color.BLACK);
		g.fillRect(0, (int) yPos, 400, fm.getHeight());
		g.setColor(Color.WHITE);
		g.drawRect(0, (int) yPos, 400, fm.getHeight());
		int xOffset = ITEM_MARGIN;
		for (int i = 0; i < items.size(); i++) {
			items.get(i).draw(g, new Vec2(xOffset, yPos));
			xOffset += items.get(i).getWidth(fm) + ITEM_MARGIN;
		}
	}
	
	public void update(Map<String, Boolean> keyState) {
		
	}
	
	public void addItem(MenuBarItem item) {
		items.add(item);
	}
}
