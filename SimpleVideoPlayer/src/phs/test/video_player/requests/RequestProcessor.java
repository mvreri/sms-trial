package phs.test.video_player.requests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Stack;

import phs.test.video_player.Logger;

/** 
 * @author Pham Hung Son
 *	This class process the request to sent to WirelessVideoServer.
 *	When too many requests are queued on to be processed,
 *	they will be discarded, only the latest one is kept.
 *
 */
public class RequestProcessor {

	private static final String SERVER_IP = "192.168.17.78";
	private static final int SERVER_PORT = 16326;

	public static final int TIME_OUT = 10000;
	private Socket socket;
	private BufferedReader inputStream;
	private PrintWriter outputStream;
	private WorkerThread workerThread;

	public RequestProcessor() throws IOException {
		this.socket = new Socket(getServerAddress(),SERVER_PORT);
		try {
			inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			outputStream = new PrintWriter(socket.getOutputStream(),true);
			workerThread = new WorkerThread();
			workerThread.start();
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


	/**
	 * Release resources
	 */
	public void stop() {
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
		workerThread.destroy();
	}

	public void addRequest(Request request) {
		workerThread.addRequest(request);
	}


	public class WorkerThread extends Thread {

		public class RequestsList extends Stack<Request> {
			private static final long serialVersionUID = 9148286267976806447L;
			public synchronized void addJob(Request request) {
				this.clear();
				super.add(request);
				this.notify();
			}

			synchronized public Request getRequest() {
				try {
					if ( this.empty() ) this.wait();
					return this.pop();
				} catch (InterruptedException e) {
					return null;
				}

			}

		}

		RequestsList jobs = null;
		public WorkerThread() {
			jobs = new RequestsList();
		}

		public void addRequest(Request request) {
			jobs.addJob(request);
		}

		@Override
		public void run() {
			while ( ! Thread.interrupted()) {
				Request request = jobs.getRequest();

				//The thread is interrupted
				if ( request == null ) break;
				
				Logger.logInfo("Sending request: " + request.reqString());
				outputStream.println(request.reqString());
				try {
					socket.setSoTimeout(TIME_OUT);
					String reply = inputStream.readLine();
					if (reply != null)
						request.processRespone(reply);
					else request.responseReplyException();
				} catch (SocketException e) {
					Logger.logError(e);
					request.responseTimeout();
				}
				catch (IOException e) {
					Logger.logError(e);
					request.responseReplyException();
				}
			}
		}

		public void destroy() {
			this.interrupt();
		}
	}


}
