package phs.test.video_player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.util.Log;

public class RemoteController {

	private static final String SERVER_IP = "192.168.17.78";
	private static final int SERVER_PORT = 16326;
	public static final String TAG = "Test_video_server";
	static final String SEPERATOR = "###";
	static final int RET_CODE_SUCCESS = 0;
	private static final int RET_CODE_FAILED = 1;

	//Respone - read the doc for server to assign the following constants
	public static final String RESPONE_SUCCESS = "Success";
	public static final String RESPONE_FAILED = "Failed";
	protected static final int TIME_OUT = 10000;


	private Socket socket;
	private BufferedReader inputStream;
	private PrintWriter outputStream;
	private IMediaServerListener listener;

	public RemoteController(IMediaServerListener listener) {
		this.listener = listener;
		createCommunicator();
	}

	private void createCommunicator() {
		try {
			socket = new Socket(getServerAddress(),SERVER_PORT);
			inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			outputStream = new PrintWriter(socket.getOutputStream(),true);
		} catch (IOException e) {
			logException(e);
		}

	}

	private void logException(IOException e) {
		Log.i(TAG,e.getClass().toString() + " : " + e.getMessage());
	}

	private InetAddress getServerAddress() {
		try {
			return InetAddress.getByName(SERVER_IP);
		} catch (UnknownHostException e) {
			return null;
		}
	}


	//Draft version, need to be designed again
	public void setup(String remoteVideoPath) {
		try {
			outputStream.println(createSetupRequest(remoteVideoPath));
			Logger.logInfo("OutputStream ");
		} catch (Exception e) {
			Logger.logError(e);
		}
		new Thread(new Runnable() {
			public void run() {
				try {
					socket.setSoTimeout(TIME_OUT);
					Logger.logInfo("Start listening for SETUP response ...");
					//					String line = Utils.blockReadline(inputStream);
					String line = inputStream.readLine();

					Logger.logInfo("Server responsed SETUP...");
					processSetupRespone(line);
				} catch (SocketException e) {
					Logger.logError(e);
				} catch (IOException e) {
					Logger.logError(e);
				}
			}
		}).start();
	}

	private String createSetupRequest(String remoteVideoPath) {
		return "Setup###"+remoteVideoPath;
	}

	protected void processSetupRespone(String line) {
		if ( line != null ) {
			Logger.logInfo("Setup respone: " + line);
			String[] words = line.split(SEPERATOR);
			if ( words.length == 1) {
				listener.onSetupRespone(RET_CODE_FAILED, null, null);
			} else if ( words.length == 3 && words[0].equals(RESPONE_SUCCESS)){ 
				listener.onSetupRespone(RET_CODE_SUCCESS, words[1].trim(), words[2].trim());
			} else {
				Logger.logError("Impossible");
				listener.onSetupRespone(RET_CODE_FAILED, null, null);
			}

		} else Logger.logInfo("Setup respone: NULL" );
	}

	//Should be worker thread and ....
	public void start() {
		outputStream.println("Start");
		new Thread(new Runnable() {
			public void run() {
				try {
					socket.setSoTimeout(TIME_OUT);
					String readLine = inputStream.readLine();
					processStartRespone(readLine);
				} catch (IOException e) {
					Logger.logError(e);
				}
			}
		}).start();
	}

	protected void processStartRespone(String readLine) {
		Logger.logInfo("Start respone: " + readLine);
		if (readLine.equals(RESPONE_SUCCESS)) {
			listener.onStartRespone(RET_CODE_SUCCESS);
		} else listener.onStartRespone(RET_CODE_FAILED);
	}

	public void stop() {
		outputStream.println("Stop");
		new Thread(new Runnable() {
			public void run() {
				try {
					socket.setSoTimeout(TIME_OUT);
					String readLine = inputStream.readLine();
					processStopRespone(readLine);
				} catch (IOException e) {
					Logger.logError(e);
				}
				releaseResources();
			}
		}).start();

	}

	protected void processStopRespone(String readLine) {
		Logger.logInfo("Stop respone: " + readLine);
		if (readLine.equals(RESPONE_SUCCESS)) {
			listener.onStopRespone(RET_CODE_SUCCESS);
		} else listener.onStopRespone(RET_CODE_FAILED);
	}

	private void releaseResources() {
		try {
			inputStream.close();			
		} catch (IOException e) {
			logException(e);
		}
		outputStream.close();
		try {
			socket.close();
		} catch (IOException e) {
			logException(e);
		}
	}


	public void pause() {
		outputStream.println("Pause");
		new Thread(new Runnable() {
			public void run() {
				try {
					socket.setSoTimeout(TIME_OUT);
					String readLine = inputStream.readLine();
					processPauseRespone(readLine);
				} catch (IOException e) {
					Logger.logError(e);
				}
			}
		}).start();		
	}

	protected void processPauseRespone(String readLine) {
		if ( readLine.equals(RESPONE_SUCCESS)) {
			listener.onPauseRespone(RET_CODE_SUCCESS);
		} else {
			listener.onPauseRespone(RET_CODE_FAILED);
		}
	}
	
	public void resume(float percent) {
		outputStream.println("Resume###"+percent);
		new Thread(new Runnable() {
			public void run() {
				try {
					socket.setSoTimeout(TIME_OUT);
					String readLine = inputStream.readLine();
					processResumeRespone(readLine);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}).start();
	}

	protected void processResumeRespone(String readLine) {
		if (readLine.startsWith(RESPONE_SUCCESS)) {
			listener.onResumeRespone(RET_CODE_SUCCESS);
		} else {
			listener.onResumeRespone(RET_CODE_FAILED);
		}
	}

	public void forward(float d) {
		// TODO Auto-generated method stub

	}

	public void backward(float d) {
		// TODO Auto-generated method stub

	}

}
