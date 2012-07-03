package phs.media_server.commands;

import java.util.HashMap;

import phs.media_server.MyUtils;


public class CommandFactory {

	public enum CommandType {
		SETUP,START,PAUSE,RESUME,SEEK_TO,STOP
	}
	private static HashMap<String,CommandType> mapStr2Type = new HashMap<String, CommandType>();
	static {
		mapStr2Type.put("setup", CommandType.SETUP);
		mapStr2Type.put("start", CommandType.START);
		mapStr2Type.put("pause", CommandType.PAUSE);
		mapStr2Type.put("resume", CommandType.RESUME);
		mapStr2Type.put("seekto", CommandType.SEEK_TO);
		mapStr2Type.put("stop", CommandType.STOP);
		
	}

	public static Command createCommand(String requestString) {
		MyUtils.assertTrue(requestString != null);
		int sepPos = requestString.indexOf(Command.SEPERATOR);
		String header = null;
		String tail = null;
		if ( sepPos == -1) {
			header = requestString;
			tail = null;
		} else {
			header = requestString.substring(0, sepPos);
			tail = requestString.substring(sepPos + Command.SEPERATOR.length() );
		}
		return newCommand(mapStr2Type.get(header.toLowerCase()),tail);
	}

	//Developer note: this could be done shorter using reflection, but don't do that, it make reading code painfully 
	private static Command newCommand(CommandType commandType, String tail) {
		if ( commandType == null) {
			MyUtils.logError("No accordingly type !");
			return null;
		}
		switch (commandType) {
		case PAUSE:
			return new PauseCommand(tail);
		case RESUME:
			return new ResumeCommand(tail);
		case SEEK_TO:
			return new SeekToCommand(tail);
		case SETUP:
			return new SetupCommand(tail);
		case START:
			return new StartCommand(tail);
		case STOP:
			return new StopCommand(tail);
		default:
			MyUtils.assertTrue(false);
			return null;
		}

	}

}