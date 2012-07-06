package phs.test.video_player.requests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Stack;

import phs.test.video_player.Logger;

/** 
 * @author Administrator
 *	This class process the request to sent to WirelessVideoServer.
 *	When too many requests are queued on to be processed,
 *	they will be discarded, only the latest one is kept.s 
 *
 */
public class RequestProcessor {



	private Socket socket;
	private BufferedReader inputStream;
	private PrintWriter outputStream;
	private WorkerThread workerThread;

	public RequestProcessor(Socket socket) {
		try {
			this.socket = socket;
			inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			outputStream = new PrintWriter(socket.getOutputStream(),true);
			workerThread = new WorkerThread();
			workerThread.start();
		} catch (IOException e) {
			Logger.logError(e);
		}
	}

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
		workerThread.addJob(request);
	}

	
	public class WorkerThread extends Thread {

		public class Joblist extends Stack<Request> {
			private static final long serialVersionUID = 9148286267976806447L;
			public void addJob(Request request) {
				synchronized (this) {
					this.clear();
					super.add(request);
					this.notify();
				}
			}

			public Request getJob() {
				synchronized (this) {
					try {
						if ( this.empty() ) this.wait();
						return this.pop();
					} catch (InterruptedException e) {
						return null;
					}
				}
			}

		}
		Joblist jobs = null;
		public WorkerThread() {
			jobs = new Joblist();
		}

		public void addJob(Request request) {
			jobs.addJob(request);
		}

		@Override
		public void run() {
			while ( ! Thread.interrupted()) {
				Request request = jobs.getJob();
				if ( request == null ) break;
				request.process();
			}
		}

		public void destroy() {
			this.interrupt();
		}
	}


}
