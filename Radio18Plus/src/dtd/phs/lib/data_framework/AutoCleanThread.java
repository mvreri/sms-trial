package dtd.phs.lib.data_framework;

import java.util.Stack;

import dtd.phs.lib.utils.Logger;

public class AutoCleanThread extends Thread {

	private static final int DEFAULT_MAX_WAITING_JOBs= 1;
	private JobsList jobs;

	public AutoCleanThread() {
		init(DEFAULT_MAX_WAITING_JOBs);
	}
	
	public AutoCleanThread(int maxWaitingJobs) {
		init(maxWaitingJobs);
	}
	

	protected void init(int maxWaitingJobs) {
		jobs = new JobsList(maxWaitingJobs);
	}
	
	@Override
	public void run() {
		super.run();
		while ( true ) {
			if (interrupted()) return;
			Runnable r = jobs.pop();
			if ( r != null ) r.run();
		}
	}

	public void stopThread() {
		jobs.clear();
		this.interrupt();
	}
	public void addJob(Runnable r) {
		jobs.add(r);
	}
	
	
	public class JobsList extends Stack<Runnable> {

		private static final long serialVersionUID = 4242683897887003726L;		
		private static final long DELAY_TIME = 100;
		private int maxWaitingJobs;

		public JobsList(int maxWaitingJobs) {
			this.maxWaitingJobs = maxWaitingJobs;
		}

		@Override
		public synchronized Runnable pop() {
			while( isEmpty() ) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					Logger.logError(e);
					return null;
				}
			}
			
			try {
				Thread.sleep(DELAY_TIME);
				//This time may be used by the server to finish whatever it is in the middle
			} catch (InterruptedException e) {
				Logger.logError(e);
				return null;
			}
			return super.pop();
		}
		
		@Override
		public synchronized boolean add(Runnable object) {
			//AUTO-CLEAN
			if ( this.size() >= maxWaitingJobs ) this.clear();
			boolean succ = super.add(object);
			this.notifyAll();
			return succ;
		}
	}

}
