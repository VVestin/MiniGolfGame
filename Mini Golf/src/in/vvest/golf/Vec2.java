package in.vvest.golf;

public class Vec2 {
	
	public final double x, y;
	
	public Vec2(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Vec2(double angle) {
		this.x = Math.cos(angle);
		this.y = Math.sin(angle);
	}
	
	public double angle() {
		return Math.atan2(y, x);
	}
	
	public double length() {
		return Math.sqrt(lengthSquared());
	}
	
	public double lengthSquared() {
		return Math.pow(x, 2) + Math.pow(y, 2);
	}
	
	public double distance(Vec2 v) {
		return Math.sqrt(distanceSquared(v));
	}
	
	public double distanceSquared(Vec2 v) {
		return Math.pow(x - v.x, 2) + Math.pow(y - v.y, 2);
	}
	
	public Vec2 scale(double amt) {
		return new Vec2(x * amt, y * amt);
	}
	
	public Vec2 rotate(double angle) {
		return (new Vec2(angle() + angle)).scale(length());
	}
	
	public Vec2 add(Vec2 v) {
		return new Vec2(x + v.x, y + v.y);
	}
	
	public Vec2 subtract(Vec2 v) {
		return new Vec2(x - v.x, y - v.y);		
	}
	
	public double dotProduct(Vec2 v) {
		return v.x * x + v.y * y;
	}
	
	public double projectedOn(Vec2 v) {
		return dotProduct(v.normalize());
	}
	
	public Vec2 normalize() {
		return new Vec2(x / length(), y / length());
	}
	
	public String toString() {
		return "Vec2: (" + x + ", " + y + ")";
	}
}
