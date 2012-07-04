package phs.test.video_player;

import android.util.Log;


public class Logger {
	

	public class Helpers {
		public static final boolean DEBUG_MODE = true;

	}

	private static final String SIL_LOG_TAG = "SIL_TAG";

	public static void logInfo(String extra) {
		if ( Helpers.DEBUG_MODE ) {
			StackTraceElement elm = Thread.currentThread().getStackTrace()[3];
			String location = extractLocation(elm);
			Log.i(SIL_LOG_TAG,extra + " == Location: " + location  );
					
		}
	}
	
	public static void logError(String extra) {
		if ( Helpers.DEBUG_MODE ) {
			StackTraceElement elm = Thread.currentThread().getStackTrace()[3];
			String location = extractLocation(elm);
			Log.e(SIL_LOG_TAG,extra + " -- Location: " + location);
		}
					
	}

	private static void logError(String extra,int extraDeepInStack) {
		if ( Helpers.DEBUG_MODE ) {
			StackTraceElement elm = Thread.currentThread().getStackTrace()[3+extraDeepInStack];
			String location = extractLocation(elm);
			Log.e(SIL_LOG_TAG,extra + " -- location: " + location);
		}
					
	}

	private static String extractLocation(StackTraceElement elm) {
		return elm.getClassName()+"."+elm.getMethodName()+" at line: " + elm.getLineNumber();
	}

	public static void logError(Exception exception) {
		logError(exception.getClass().toString() + " - with message: " + exception.getMessage(),1);
	}
	

}
