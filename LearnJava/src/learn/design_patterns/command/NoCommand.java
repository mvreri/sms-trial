package learn.design_patterns.command;

public class NoCommand implements Command {

	@Override
	public void execute() {
		System.out.println("Empty");
	}

	@Override
	public void undo() {
		System.out.println("Nothing to undo");
	}

}
