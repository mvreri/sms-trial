package phs.media_server;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.ServerSocket;

import junit.framework.Assert;

public class MyUtils {
	public enum CodeModes {DEV,TEST,PRODUCT};
	static public final CodeModes CURRENT_CODE_MODE = CodeModes.DEV;
	private static final String TAG = "PHS_MEDIA_SERVER";
	public static void verifyTrue(boolean b) {
		if (CURRENT_CODE_MODE == CodeModes.DEV )
			Assert.assertTrue(b);
		else if ( CURRENT_CODE_MODE == CodeModes.TEST ) {
			logMessage("Assert false");
		}
	}
	public static void logErrorTrace() {
		System.err.println("TODO: error trace is logged");
	}

	public static void logInfo(String message) {
		logMessage(message);
	}

	private static void logMessage(String message) {
		if ( CURRENT_CODE_MODE != CodeModes.PRODUCT) {
			StackTraceElement elm = Thread.currentThread().getStackTrace()[3];
			String location = extractLocation(elm);
			System.out.println(TAG+": " + message + " == Location: " + location );
		}
	}

	public static void logError(String message) {
		if ( CURRENT_CODE_MODE != CodeModes.PRODUCT) {
			StackTraceElement elm = Thread.currentThread().getStackTrace()[3];
			String location = extractLocation(elm);
			System.err.println(TAG+": " + message + " == Location: " + location );
		}
		
	}
	
	public static void logError(Exception e) {
		logError(e.getClass().toString() + ":" + e.getMessage());
	}

	private static String extractLocation(StackTraceElement elm) {
		return elm.getClassName()+"."+elm.getMethodName()+" at line: " + elm.getLineNumber();
	}
	
	private static final int MIN_PORT = 50000;
	private static final int MAX_PORT = 65535;
	/**
	 * Find the available port in range [MIN_PORT,MAX_PORT]
	 * @return
	 */
	public static int findFreePort() {
		for(int port = MIN_PORT ; port <= MAX_PORT ; port++) {
			try {
				ServerSocket serverSocket = new ServerSocket(port);
				serverSocket.close();
				return port;
			} catch (IOException e) {
				MyUtils.logInfo("Port " + port + " is reserved !");
			}
		}
		return -1;
	}

//	private static final int BUFFER_SIZE = 512;
//	/**
//	 * This method will block util the input is ready to be read,
//	 * The buffer size currently is 512 bytes
//	 * @param inputStream
//	 * @return The line read from the stream
//	 * @throws IOException 
//	 */	
//	public static String blockReadline(BufferedReader inputStream) throws IOException {
//		char[] buffer = new char[BUFFER_SIZE];
//		int cntRead = inputStream.read(buffer);
//		if ( cntRead == -1) return null;
//		String line = new String(buffer).trim();
//		return line;
//	}
//


}
