package phs.test.video_player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import phs.test.video_player.requests.Request;
import phs.test.video_player.requests.RequestFactory;
import phs.test.video_player.requests.RequestProcessor;
import android.location.Address;
import android.util.Log;

//TODO: refactor !
public class RemoteController {

	private static final String SERVER_IP = "192.168.17.78";
	private static final int SERVER_PORT = 16326;
	public static final String TAG = "Test_video_server";

	private Socket socket;
	private IMediaServerListener listener;
	private RequestProcessor requestWorker;

	public RemoteController(IMediaServerListener listener) {
		this.listener = listener;
		createCommunicator();
	}

	private void createCommunicator() {
		try {
			socket = new Socket(getServerAddress(),SERVER_PORT);
			requestWorker = new RequestProcessor(socket);
		} catch (IOException e) {
			Logger.logError(e);
		}

	}


	private InetAddress getServerAddress() {
		try {
			return InetAddress.getByName(SERVER_IP);
		} catch (UnknownHostException e) {
			return null;
		}
	}


	public void setup(String remoteVideoPath) {
		Request request = RequestFactory.createRequest("setup",remoteVideoPath,listener);
		requestWorker.addRequest(request);
	}


	public void start() {
		Request request = RequestFactory.createRequest("start", null, listener);
		requestWorker.addRequest(request);
	}


	public void stop() {
		Request request = RequestFactory.createRequest("stop", null, listener);
		requestWorker.addRequest(request);
		asdsaf();
		//TODO: releaseResources();
	
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
			Logger.logError(e);
		}
		outputStream.close();
		try {
			socket.close();
		} catch (IOException e) {
			Logger.logError(e);
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
					Logger.logError(e);
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
					Logger.logError(e);
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
