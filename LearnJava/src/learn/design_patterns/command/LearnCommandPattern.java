package learn.design_patterns.command;

public class LearnCommandPattern {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		RemoteControl remote = new RemoteControl(); //invoker
		
		Light light = new Light();
		remote.setCommand(0,new LightOnCommand(light),new LightOffCommand(light));

		GarageDoor gdoor = new GarageDoor();
		remote.setCommand(1,new GarageDoorOpenCommand(gdoor),new GarageDoorCloseCommand(gdoor));
		
		remote.onButtonPushed(0);
		remote.onButtonPushed(1);
		remote.offButtonPushed(1);
		remote.offButtonPushed(0);
	}

}
