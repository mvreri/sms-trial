package learn.design_patterns.command;

public class LightOnCommand implements Command {

	private Light light;

	public LightOnCommand(Light light) {
		this.light = light;
	}
	
	public void execute() {
		light.on();
	}

	@Override
	public void undo() {
		light.off();
	}
}
