package com.vsm.radio18.data.db;

import java.util.LinkedList;

import dtd.phs.lib.utils.Logger;

public class QueryWorker {
	private volatile static AutoCleanThread workerThread = null;
	private static  AutoCleanThread getWorkerThread() {
		if ( workerThread == null ) {
			workerThread = new AutoCleanThread();
			workerThread.start();
		}
		return workerThread;
	}

	public static void add(final Runnable job) {
		getWorkerThread().addJob(job);
	}

	public static class AutoCleanThread extends Thread {

		private static final String THREAD_NAME = "QueryWorker";
		private static int instanceCount = 0;
		private JobsList jobs;

		public AutoCleanThread() {
			super(THREAD_NAME + (instanceCount++));
			jobs = new JobsList();
		}

		@Override
		public void run() {
			super.run();
			while ( true ) {
				if (interrupted()) return;
				Runnable r = jobs.poll();
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

		public class JobsList extends LinkedList<Runnable> {

			private static final long serialVersionUID = 4881547420141793987L;
			private static final int MAX_WAITING_COUNT = 10;
			private static final int WARNING_COUNT = 3;

			@Override
			public synchronized Runnable poll() {
				while( isEmpty() ) {
					try {
						this.wait();
					} catch (InterruptedException e) {
						Logger.logError(e);
						return null;
					}
				}
				return super.poll();
			}

			@Override
			public synchronized boolean add(Runnable object) {
				//AUTO-CLEAN
				if ( this.size() >= MAX_WAITING_COUNT ) this.clear();
				if ( this.size() >= WARNING_COUNT ) {
					Logger.logError("Too many query !");
				}
				boolean succ = super.add(object);
				this.notifyAll();
				return succ;
			}
		}

	}

}
