package learn.design_patterns.command;

/**
 * 
 * @author Son Pham
 * Simple 1 button "On" remote
 */
public class RemoteControl {
	private static final int NUM_DEVICEs = 7;
	Command[] onCommands = new Command[NUM_DEVICEs];
	Command[] offCommands = new Command[NUM_DEVICEs];
	
	Command undoSlot = null; //TODO: here !
	private Command undoCommand;
	public RemoteControl() {
		Command noCommand = new NoCommand();
		for(int i = 0 ; i < NUM_DEVICEs ; i++ ) {
			onCommands[i] = noCommand;
			offCommands[i] = noCommand;
		}
		undoCommand = noCommand;
	}
	public void setCommand(int slot, Command onCommand, Command offCommand) {
		onCommands[slot] = onCommand;
		offCommands[slot] = offCommand;
	}

	public void onButtonPushed(int slot) {
		onCommands[slot].execute();
		undoCommand = onCommands[slot];
	}
	
	public void offButtonPushed(int slot) {
		offCommands[slot].execute();
		undoCommand = offCommands[slot];
	}
	
	public void undoPushed() {
		undoCommand.execute(); //multiple pushed ???? WRONG Answer !
	}

}
