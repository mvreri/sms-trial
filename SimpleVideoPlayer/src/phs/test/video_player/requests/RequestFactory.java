package phs.test.video_player.requests;

import java.lang.reflect.Constructor;
import java.util.HashMap;

import phs.test.video_player.Logger;

public class RequestFactory {

	private static HashMap<String, Class<?>> mapStr2Type = new HashMap<String, Class<?>>();
	static {
		mapStr2Type.put("setup", SetupRequest.class);
		mapStr2Type.put("start", StartRequest.class);
		mapStr2Type.put("pause", PauseRequest.class);
		mapStr2Type.put("resume", ResumeRequest.class);
		mapStr2Type.put("seekto", SeekToRequest.class);
		mapStr2Type.put("stop", StopRequest.class);
		mapStr2Type.put("getduration", GetDurationRequest.class);
	}

	public static Request createRequest(String requestType, String parameters) {
		
		Class<?> requestClass = mapStr2Type.get(requestType);
		if ( requestClass == null) {
			Logger.logError("No according type !");
		}
		try {
			Constructor<?> ctor = requestClass.getConstructor(String.class,String.class);
			return (Request) ctor.newInstance(requestType,parameters);
		} catch (Exception e) {
			return null;
		}
		
	}

}
