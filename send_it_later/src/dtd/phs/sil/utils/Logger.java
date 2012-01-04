package dtd.phs.sil.utils;

import android.util.Log;

public class Logger {
	

	public static void logInfo(String extra) {
		if ( Helpers.DEBUG_MODE ) {
			StackTraceElement elm = Thread.currentThread().getStackTrace()[3];
			String location = extractLocation(elm);
			Log.i("[phs.vne]",extra + " == Location: " + location  );
					
		}
	}
	
	public static void logError(String extra) {
		if ( Helpers.DEBUG_MODE ) {
			StackTraceElement elm = Thread.currentThread().getStackTrace()[3];
			String location = extractLocation(elm);
			Log.e("[phs.vne]",extra + " -- Location: " + location);
		}
					
	}

	private static void logError(String extra,int extraDeepInStack) {
		if ( Helpers.DEBUG_MODE ) {
			StackTraceElement elm = Thread.currentThread().getStackTrace()[3+extraDeepInStack];
			String location = extractLocation(elm);
			Log.e("[phs.vne]",extra + " -- location: " + location);
		}
					
	}

	private static String extractLocation(StackTraceElement elm) {
		return elm.getClassName()+"."+elm.getMethodName()+" at line: " + elm.getLineNumber();
	}

	public static void logError(Exception exception) {
		logError(exception.getClass().toString() + " - with message: " + exception.getMessage(),1);
	}
	

}
