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

//TODO: refactor !
public class RemoteController {

	private static final String SERVER_IP = "192.168.17.78";
	private static final int SERVER_PORT = 16326;
	public static final String TAG = "Test_video_server";
	static final String SEPERATOR = "###";

	//Response code for the listener of this controller
	static final int RET_CODE_SUCCESS = 0;
	static final int RET_CODE_FAILED = 1;
	static final int RET_CODE_TIMEOUT = 2;

	//Response - read the doc for server to assign the following constants
	public static final String RESPONE_SUCCESS = "success";
	public static final String RESPONE_FAILED = "failed";
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


	//TODO: Draft version, need to be designed again
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
					//TODO: It could be the case that this respone is not setup respone -> fix it
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
			if ( words.length == 4
					&& words[0].toLowerCase().equals("setup")
					&& words[1].toLowerCase().equals(RESPONE_SUCCESS)){ 
				listener.onSetupRespone(RET_CODE_SUCCESS, words[2].trim(), words[3].trim());
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
				} catch (SocketException e) {
					Logger.logError(e);
					listener.onStartRespone(RET_CODE_TIMEOUT);
				} catch (IOException e) {
					Logger.logError(e);					
				}
			}
		}).start();
	}

	protected void processStartRespone(String readLine) {
		Logger.logInfo("Start respone: " + readLine);
		String[] words = readLine.split(SEPERATOR);
		if ( words.length == 2 
				&& words[0].toLowerCase().equals("start")
				&& words[1].toLowerCase().equals(RESPONE_SUCCESS)
				) {
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
				} catch (SocketException e) {
					Logger.logError(e);
					listener.onStopRespone(RET_CODE_TIMEOUT);
				} catch (IOException e) {
					Logger.logError(e);
				}
				releaseResources();
			}
		}).start();

	}

	protected void processStopRespone(String readLine) {
		Logger.logInfo("Stop respone: " + readLine);
		String[] words = readLine.split(SEPERATOR);
		if (words.length==2 && words[0].toLowerCase().equals("stop") && words[1].toLowerCase().equals(RESPONE_SUCCESS)) {
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
				} catch (SocketException e) {
					Logger.logError(e);
					listener.onPauseRespone(RET_CODE_TIMEOUT);
				} catch (IOException e) {
					Logger.logError(e);
				}
			}
		}).start();		
	}

	protected void processPauseRespone(String readLine) {
		String[] words = readLine.split(SEPERATOR);
		if (words.length==2 && words[0].toLowerCase().equals("pause") && words[1].toLowerCase().equals(RESPONE_SUCCESS)) {
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
				} catch (SocketException e) {
					Logger.logError(e);
					listener.onResumeRespone(RET_CODE_TIMEOUT);
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}).start();
	}

	protected void processResumeRespone(String readLine) {
		String[] words = readLine.split(SEPERATOR);
		if (words.length==2 && words[0].toLowerCase().equals("resume") && words[1].toLowerCase().equals(RESPONE_SUCCESS)) {
			listener.onResumeRespone(RET_CODE_SUCCESS);
		} else {
			listener.onResumeRespone(RET_CODE_FAILED);
		}
	}

	public void getDuration() {
		outputStream.println("GetDuration");
		new Thread(new Runnable() {
			public void run() {
				try {
					socket.setSoTimeout(TIME_OUT);
					String readLine = inputStream.readLine();
					processGetDurationResponse(readLine);
				} catch (SocketException e) {
					listener.onGetDurationResponse(RET_CODE_FAILED,0);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

	protected void processGetDurationResponse(String readLine) {
		String[] words = readLine.split(SEPERATOR);
		if (words.length==2 && words[0].toLowerCase().equals("getduration") && words[1].toLowerCase().equals(RESPONE_SUCCESS)) {
			listener.onGetDurationResponse(RET_CODE_SUCCESS, Integer.parseInt(words[2]));
		} else {
			listener.onGetDurationResponse(RET_CODE_FAILED, 0);
		}

	}

	public void seekTo(float next) {
		String command = "SeekTo###"+next;
		Logger.logInfo("Send command: " + command);
		outputStream.println(command);
		new Thread(new Runnable() {
			public void run() {
				try {
					socket.setSoTimeout(TIME_OUT);
					String readLine = inputStream.readLine();
					processSeekResponse(readLine);
				} catch (SocketException e) {
					listener.onSeekResponse(RET_CODE_FAILED);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	protected void processSeekResponse(String readLine) {
		String[] words = readLine.split(SEPERATOR);
		if (words.length==2 && words[0].toLowerCase().equals("seekto") && words[1].toLowerCase().equals(RESPONE_SUCCESS)) {
			listener.onSeekResponse(RET_CODE_SUCCESS);
		} else {
			listener.onSeekResponse(RET_CODE_FAILED);
		}
	}

}
