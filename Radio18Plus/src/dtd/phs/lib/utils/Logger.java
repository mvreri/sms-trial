package dtd.phs.lib.utils;

import junit.framework.Assert;
import android.util.Log;


public class Logger {
	

	private static final String LOG_TAG = "PHS_LOG";

	protected enum CodeModes {DEBUG, TEST, PRODUCTION};
	protected static CodeModes CODE_MODE = CodeModes.DEBUG;

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
		logError(exception.toString(),1);
	}
	
	public static void assertCondition(boolean condition, String failedMessage) {
		if ( CODE_MODE == CodeModes.DEBUG  )
			Assert.assertTrue(failedMessage, condition);
		else if ( CODE_MODE == CodeModes.TEST) {
			Logger.logError("Assert failed: " + failedMessage);
		} else {
			//do nothing in PRODUCTION
		}
	}
	


}
