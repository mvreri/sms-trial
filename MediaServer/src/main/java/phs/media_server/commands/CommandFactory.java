package phs.media_server.commands;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import phs.media_server.Logger;


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

	private static Command newCommand(String header, String tail) {
		Class<?> commandClass = mapStr2Type.get(header);
		if ( commandClass == null) {
			Logger.logError("No accordingly type !");
			return null;
		}
		try {
			Constructor<?> ctor = commandClass.getConstructor(String.class,String.class);
			return (Command) ctor.newInstance(header,tail);
		} catch (Exception e) {
			Logger.logError(e);
			return null;
		}
	}

	public static Command createCommand(String requestString) {
		Logger.verifyTrue(requestString != null);
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
		Logger.logInfo(mapStr2Type.get(header.toLowerCase()).getName() +" is to be created !");
		return newCommand(header.toLowerCase(),tail);
	}



}