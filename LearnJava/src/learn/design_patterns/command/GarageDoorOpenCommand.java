package learn.design_patterns.command;

public class GarageDoorOpenCommand implements Command{

	private GarageDoor gdoor;

	public GarageDoorOpenCommand(GarageDoor gdoor) {
		this.gdoor = gdoor;
	}

	public void execute() {
		gdoor.up();
	}

	@Override
	public void undo() {
		gdoor.down(); //not neccessary ! it's not "state" undo, it failed !
	}

}
