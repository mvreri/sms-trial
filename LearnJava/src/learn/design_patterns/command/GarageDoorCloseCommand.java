package learn.design_patterns.command;

public class GarageDoorCloseCommand implements Command{

	private GarageDoor gdoor;

	public GarageDoorCloseCommand(GarageDoor gdoor) {
		this.gdoor = gdoor;
	}

	public void execute() {
		gdoor.down();
	}

	@Override
	public void undo() {
		gdoor.up();
	}

}
