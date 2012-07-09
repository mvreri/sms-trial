package phs.media_server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import phs.media_server.commands.Command;
import phs.media_server.commands.CommandFactory;
/**
 * @author Pham Hung Son
 *	This class is the message server: it serves as communicator between server & clients 
 *	Note that, the streaming server uses another port </br>
 *	- It opens a socket on the server, listens to clients. </br>
 *	- When a client connects: </br>
 *	 + Create a session to handle that client
 *	 + The session will handle communication between the client & media server </br>
 *
 *	- Type of requests (all requests/responses are case insensitive): </br> 
 *	 + SETUP(Full_file_path): Prepare to stream the local file</br>
 * 	  - Request: Setup###[file_path] , e.g: Setup###D:\Movies\720p.mp4 </br>
 * 	  - Respone: A string with this format </br>
 *			[Setup###Success###int STREAM_SERVER_PORT###String STREAM_ID] </br>
 *			[Setup###Failed]</br>
 *
 *	 + START(): start the streaming server </br>
 *	  * Request: Start
 *	  * Respone: 
 *			Start###Success </br> 
 *			Start###Failed </br>
 *
 *	 + PAUSE(): pause the streaming video</br>
 *	  * Request: Pause
 *	  * Respone: Pause###Success - Pause###Failed  </br>
 *
 *	 + RESUME(): resume the streaming video from the percent position</br>
 *	  * Request: Resume </br>
 *	  * Respone: Resume###Success - Resume###Failed </br>
 *
 *	 + SEEK_TO(float percent): with percent in the range [0,1] </br>
 *	  * Request: SeekTo###[percent] , e.g: SeekTo##0.3 </br>
 *	  * Respone: SeekTo###Success - SeekTo###Failed  </br>
 *
 *	 + STOP(): stop the streaming server (This message server still running !) </br>
 *	  * Request: Stop </br>
 *	  * Response: Stop###Success - Setup###Failed </br> </br>
 *
 *	 + GET_DURATION(): get video duration (can only be called after video already playing) 
 *	  * Request: GetDuration
 *	  * Response: 
 *			GetDuration###Success###int duration
 *			GetDuration###Failed  
 *	 + Anything else: respone "Unknown command" </br>
 *		
 */
public class WirelessVideoServer {

	private static final int SERVER_PORT = 16326;

	//Return codes for startService()
	static final int START_SUCCESS = 0;
	public static final int ERR_SOCK_OPEN_FAILED = 1;

	private ServerSocket serverSocket;
	private SessionHandler sessionHandler;

	public WirelessVideoServer() {}

	/**
	 * 
	 * @return 
	 * 	0: if success
	 * 	1: cannot open socket at MESSAGE_PORT
	 */
	public int startService() {
		try {
			serverSocket = new ServerSocket(SERVER_PORT);
			sessionHandler = new SessionHandler();
			sessionHandler.start();
			return START_SUCCESS;
		} catch (IOException e) {
			Logger.logInfo("Cannot open server socket at port: " + SERVER_PORT);
			return ERR_SOCK_OPEN_FAILED;
		}
	}

	public void stopService() {
		sessionHandler.interrupt();
		try {
			serverSocket.close();
		} catch (IOException e) {
			Logger.logInfo("Cannot stop the server !");
			Logger.logError(e);
		}
	}

	public class SessionHandler extends Thread {
		private ArrayList<MediaSession> runningSessions = null;

		public SessionHandler() {
			this.runningSessions = new ArrayList<WirelessVideoServer.MediaSession>();
		}

		@Override
		public void run() {
			try {
				while ( ! Thread.interrupted() ) {
					Socket clientSocket = null;
					try {
						clientSocket = serverSocket.accept();
						MediaSession session = new MediaSession(clientSocket);
						session.start();
						runningSessions .add(session);
					} catch (SocketException e) {
						Logger.logError(e);
						Logger.logInfo("SecurityException : may be server is closing ...");
					} catch (IOException e) {
						Logger.logError(e);
					}
				}
			} catch (SecurityException e) {
				Logger.logInfo("SecurityException : may be server is closing ...");
			} finally {
				tearDownSessions();
			}

		}

		private void tearDownSessions() {
			for(MediaSession session : runningSessions ) {
				Logger.verifyTrue(session != null);
				session.interrupt();
			}
		}
	}

	public class MediaSession extends Thread {
		
		private Socket clientSocket;
		private BufferedReader inputStream;
		private PrintWriter outputStream;
		private IMediaServer mediaServer;
		
		public MediaSession(Socket clientSocket) {
			this.clientSocket = clientSocket;
		}

		@Override
		public void run() {
			try {
				initResources();
				processCommands();
			} catch (IOException e) {
				Logger.logError(e);				
			} finally {
				releaseResources();
			}
		}

		private void initResources() throws IOException {
			inputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			outputStream = new PrintWriter(clientSocket.getOutputStream(),true);
			mediaServer = new VLC_MediaServer();
		}

		private void processCommands() throws IOException {
			while ( ! Thread.interrupted() ) {
				
				String requestString = inputStream.readLine();
				
				if ( requestString != null) {
					Command command = CommandFactory.createCommand(requestString);
					if ( command != null) {
						command.process(mediaServer);
						Logger.logInfo("Respone:" + command.getRespone());
						outputStream.println(command.getRespone());
					} else {
						outputStream.println(Command.UNKNOWN_COMMAND);
					}
				} else {						
					Logger.logInfo("Client closes the socket !");
					break;
				}
				
			}
		}
		
		private void releaseResources() {
			outputStream.close();
			try {
				inputStream.close();
			} catch (IOException e) {
				Logger.logError(e);
			}
			try {
				if (clientSocket != null) clientSocket.close();
			} catch (IOException e) {
				Logger.logError(e);
			}
			inputStream = null;
			outputStream = null;
			clientSocket = null;
			mediaServer.stop();
			mediaServer = null;
		}		
	}

}
