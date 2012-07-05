package phs.media_server.commands;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import phs.media_server.MyUtils;


public class CommandFactory {

	//	public enum CommandType {
	//		SETUP,START,PAUSE,RESUME,SEEK_TO,STOP
	//	}

	//Mapping the 
	private static HashMap<String,Class<?>> mapStr2Type = new HashMap<String, Class<?>>();
	static {
		mapStr2Type.put("setup", SetupCommand.class);
		mapStr2Type.put("start", StartCommand.class);
		mapStr2Type.put("pause", PauseCommand.class);
		mapStr2Type.put("resume", ResumeCommand.class);
		mapStr2Type.put("seekto", SeekToCommand.class);
		mapStr2Type.put("stop", StopCommand.class);
		mapStr2Type.put("getduration", GetDurationCommand.class);
	}

	private static Command newCommand(Class<?> commandClass, String tail) {
		if ( commandClass == null) {
			MyUtils.logError("No accordingly type !");
			return null;
		}
		try {
			Constructor ctor = commandClass.getConstructor(String.class);
			return (Command) ctor.newInstance(tail);
		} catch (Exception e) {
			MyUtils.logError(e);
			return null;
		}
		//		switch (commandType) {
		//		case PAUSE:
		//			return new PauseCommand(tail);
		//		case RESUME:
		//			return new ResumeCommand(tail);
		//		case SEEK_TO:
		//			return new SeekToCommand(tail);
		//		case SETUP:
		//			return new SetupCommand(tail);
		//		case START:
		//			return new StartCommand(tail);
		//		case STOP:
		//			return new StopCommand(tail);
		//		default:
		//			MyUtils.assertTrue(false);
		//			return null;
		//		}		
	}

	public static Command createCommand(String requestString) {
		MyUtils.verifyTrue(requestString != null);
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
		MyUtils.logInfo(mapStr2Type.get(header.toLowerCase()).getName() +" is to be created !");
		return newCommand(mapStr2Type.get(header.toLowerCase()),tail);
	}



}