package dtd.phs.chatexperiment_phs;

import android.util.Log;


public class Logger {
	

	public enum CodeModes { DEBUG, TEST , PRODUCTION };

	private static final String LOG_TAG = "PHS_NHACSO";
	private static final CodeModes CODE_MODE = CodeModes.DEBUG;

	public static void logInfo(String extra) {
		if ( CODE_MODE == CodeModes.DEBUG || CODE_MODE == CodeModes.TEST) {
			StackTraceElement elm = Thread.currentThread().getStackTrace()[3];
			String location = extractLocation(elm);
			Log.i(LOG_TAG,extra + " == Location: " + location  );
					
		}
	}
	
	public static void logError(String extra) {
		if ( CODE_MODE == CodeModes.DEBUG || CODE_MODE == CodeModes.TEST) {
			StackTraceElement elm = Thread.currentThread().getStackTrace()[3];
			String location = extractLocation(elm);
			Log.e(LOG_TAG,extra + " -- Location: " + location);
		}
	}

	private static void logError(String extra,int extraDeepInStack) {
		if ( CODE_MODE == CodeModes.DEBUG || CODE_MODE == CodeModes.TEST) {
			StackTraceElement elm = Thread.currentThread().getStackTrace()[3+extraDeepInStack];
			String location = extractLocation(elm);
			Log.e(LOG_TAG,extra + " -- location: " + location);
		}
					
	}

	private static String extractLocation(StackTraceElement elm) {
		return elm.getClassName()+"."+elm.getMethodName()+" at line: " + elm.getLineNumber();
	}

	public static void logError(Exception exception) {
		logError(exception.getClass().toString() + " - with message: " + exception.getMessage(),1);
	}
	

}
