package learn.design_patterns.command;

public class GarageDoor {
	public void up() {
		System.out.println("Up: open the garage door");
	}
	public void down() {
		System.out.println("Down: close the garage door");
	}
	public void tightDoorLightOn() {
		System.out.println("Turn on door light");
	}
	public void tightDoorLightOff() {
		System.out.println("Turn off door light");
	}
}
