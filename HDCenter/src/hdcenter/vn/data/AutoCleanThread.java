package hdcenter.vn.data;

import hdcenter.vn.utils.Logger;

import java.util.Stack;

public class AutoCleanThread extends Thread {

	private JobsList jobs;

	public AutoCleanThread() {
		jobs = new JobsList();
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
		private static final int MAX_WAITING_COUNT = 2;

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
			return super.pop();
		}
		
		@Override
		public synchronized boolean add(Runnable object) {
			//AUTO-CLEAN
			if ( this.size() >= MAX_WAITING_COUNT ) this.clear();
			boolean succ = super.add(object);
			this.notifyAll();
			return succ;
		}
	}

}
