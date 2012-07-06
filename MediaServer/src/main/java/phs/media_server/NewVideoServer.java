package phs.media_server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import phs.media_server.commands.Command;
import phs.media_server.commands.CommandFactory;

public class NewVideoServer {




	private static final int SERVER_PORT = 16326;

	//Return codes for startService()
	private static final int START_SUCCESS = 0;
	private static final int ERR_SOCK_OPEN_FAILED = 1;

	private ServerSocket serverSocket;
	private SessionHandler sessionHandler;

	public NewVideoServer(Object listener) {}

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

	public void stopService() throws IOException {
		sessionHandler.interrupt();
		serverSocket.close();
	}

	public class SessionHandler extends Thread {
		private ArrayList<MediaSession> runningSessions = null;

		public SessionHandler() {
			this.runningSessions = new ArrayList<NewVideoServer.MediaSession>();
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
		}		
	}

}
