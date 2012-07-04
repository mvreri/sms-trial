package phs.media_server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import phs.media_server.commands.Command;
import phs.media_server.commands.CommandFactory;

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
 *
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

		@Override
		public void run() {
			initResources();
			try {
				serverSocket = new ServerSocket(MESSAGE_PORT);
				clientSocket = serverSocket.accept();
				inputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				outputStream = new PrintWriter(clientSocket.getOutputStream(), true);
				mediaServer = new VLC_MediaServer();
				while (! Thread.interrupted()) {
					String requestString = inputStream.readLine();
					//TODO: should the command be queued here ? Or it is queued already ?
					Command command = CommandFactory.createCommand(requestString);
					if ( command != null) {
						command.process(mediaServer);
						outputStream.println(command.getRespone());
					}  else {
						outputStream.println(UNKNOWN_COMMAND);
					}
					command = null;
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
