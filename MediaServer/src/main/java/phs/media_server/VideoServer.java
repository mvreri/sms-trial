package phs.media_server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import phs.media_server.commands.Command;
import phs.media_server.commands.CommandFactory;
import sun.misc.Cleaner;

/**
 * @author Pham Hung Son
 *	This class is the message server: it serves as communicator between server & client 
 *	Note that, the streaming server uses another port </br>
 *	This message server deals only with 1 client
 *	- It opens a socket on the server, listens to the client. </br>
 *	- When client request something: </br>
 *	 + it will pass command to media server </br>
 *	 + respone to the the client </br>
 *	- Requests: </br> 
 *	 + SETUP(Full_file_path): Prepare to stream the local file</br>
 * 	  - Request: Setup###[file_path] , e.g: Setup###D:\Movies\720p.mp4 </br>
 * 	  - Respone: A string with this format </br>
 *			[Success###int STREAM_SERVER_PORT###String STREAM_ID] </br>
 *			Failed</br>
 *
 *	 + START(): start the streaming server </br>
 *	  * Request: Start
 *	  * Respone: Success - Failed </br>
 *
 *	 + PAUSE(): pause the streaming video</br>
 *	  * Request: Pause
 *	  * Respone: Success - Failed  </br>
 *
 *	 + RESUME(float percent): resume the streaming video from the percent position</br>
 *	  * Request: Resume
 *	  * Respone: Success###0.3 - Failed </br>
 *
 *	 + SEEK_TO(float percent): with percent in the range [0,1]
 *	  * Request: SeekTo###[percent] , e.g: SeekTo##0.3
 *	  * Respone: Success - Failed 
 *
 *	 + STOP(): stop the streaming server (This message server still running !)
 *	  * Request: Stop
 *	  * Respone: Success - Failed 
 *
 *	 + Anything else: respone "Unknown command"
 *		
 */
public class VideoServer {

	public static final int ERR_CODE_SUCCESS = 0;

	//TODO: what if port reserved ?
	public static final int MESSAGE_PORT = 16326;
	public static final String UNKNOWN_COMMAND = "Unknown command";

	private WorkerThread workerThread;

	public VideoServer() {
	}

	public int startService() {
		workerThread = new WorkerThread();
		workerThread.start();
		return ERR_CODE_SUCCESS;
	}

	public class WorkerThread extends Thread {
		private ServerSocket serverSocket;
		private Socket clientSocket;
		private BufferedReader inputStream;
		private PrintWriter outputStream;
		private IMediaServer mediaServer;

		//TODO: This design doesn't work when there're more than one session (stop and reconnect again)
		//It should be handled as session !
		@Override
		public void run() {
			initResources();
			try {
				serverSocket = new ServerSocket(MESSAGE_PORT);
				MyUtils.logInfo("Waiting for client to connect ...");
				clientSocket = serverSocket.accept();
				MyUtils.logInfo("Client connected: " + clientSocket.getInetAddress().getHostName());
				inputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				outputStream = new PrintWriter(clientSocket.getOutputStream(),true);
				mediaServer = new VLC_MediaServer();
				while (! Thread.interrupted()) {
					MyUtils.logInfo("Main LOOP ....");
					String requestString = inputStream.readLine();
					if ( requestString != null) {
						//TODO: should the command be queued here ? Or it is queued already ?
						Command command = CommandFactory.createCommand(requestString);
						if ( command != null) {
							MyUtils.logInfo("Command is being processed ...");
							command.process(mediaServer);
							MyUtils.logInfo("Respone:" + command.getRespone());
							outputStream.println(command.getRespone());
						}  else {
							outputStream.println(UNKNOWN_COMMAND);
						}
						command = null;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				releaseResources();
			}
		}

		private void initResources() {
			serverSocket = null;
			clientSocket = null;
			inputStream = null;
			outputStream = null;

		}

		private void releaseResources() {
			try {
				if (serverSocket != null) serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (clientSocket != null) clientSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public int stopService() {
		workerThread.interrupt();
		return ERR_CODE_SUCCESS;
	}
}
