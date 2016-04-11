package in.vvest.golf;

public enum ObstacleID {
	
	CIRCLE_HILL(0), CIRCLE_LINE(1), CIRCLE_WALL(2), HOLE(3), LINE(4), RECT_GRASS(5), RECT_HILL(6), WALL(7);
	
	private byte id;
	
	private ObstacleID(int id) {
		this.id = (byte) id;
	}
	
	public byte getID() {
		return id;
	}
	
}
