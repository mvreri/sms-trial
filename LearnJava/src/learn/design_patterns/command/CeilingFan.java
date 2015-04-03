package learn.design_patterns.command;

public class CeilingFan {
	public static final int HIGH = 3;
	public static final int MEDIUM = 2;
	public static final int LOW = 1;
	public static final int OFF = 0;
	String location;
	int speed = 0;
	public CeilingFan(String location) {
		this.location = location;
		speed = OFF;
	}
	
	public void high() {
		this.speed = HIGH;
	}
	public void medium() {
		this.speed = MEDIUM;
	}
	public void low() {
		this.speed = LOW;
	}
	public void off() {
		this.speed = OFF;
	}
	public int getSpeed() {
		return this.speed;
	}

}
