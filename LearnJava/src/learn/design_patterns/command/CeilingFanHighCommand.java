package learn.design_patterns.command;

public class CeilingFanHighCommand implements Command {

	private int prevSpeed;
	private CeilingFan cfan;

	public CeilingFanHighCommand(CeilingFan cfan) {
		this.cfan = cfan;
	}

	@Override
	public void execute() {
		prevSpeed = cfan.getSpeed();
		cfan.high();
	}

	@Override
	public void undo() {
		switch (prevSpeed) {
		case CeilingFan.HIGH:
			cfan.high();
			break;
		case CeilingFan.MEDIUM:
			cfan.medium();
			break;
		case CeilingFan.LOW:
			cfan.low();
			break;
		case CeilingFan.OFF:
			cfan.off();
			break;
		default:
			Helpers.assertTrue(false);
		}
	}

}
