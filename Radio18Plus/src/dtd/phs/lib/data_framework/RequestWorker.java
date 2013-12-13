package dtd.phs.lib.data_framework;

import java.lang.ref.WeakReference;

import android.os.Handler;
import dtd.phs.lib.utils.Helpers;

public class RequestWorker {

	private volatile static AutoCleanThread robotWorkerThread = null;
	private volatile static AutoCleanThread storeWorkerThread = null;

	public static void addRequest(
			final IRequest request,
			final IDataListener listener,
			final Handler handler) {
		AutoCleanThread worker = getStoreWorkerThread();
		addJob(request, listener, handler, worker);
	}

	protected static void addJob(final IRequest request,
			IDataListener listener, final Handler handler,
			AutoCleanThread worker) {
		final WeakReference<IDataListener> wListener = new WeakReference<IDataListener>(listener);
		Helpers.assertCondition(handler != null);
		worker.addJob(
				new Runnable() {
					@Override
					public void run() {
						try {
							final Object data = request.requestData();
							handler.post(new Runnable() {
								@Override
								public void run() {
									IDataListener listener = wListener.get();
									if ( listener != null)
										listener.onCompleted(data);
								}
							});

						} catch (final Exception e) {
							
							handler.post(new Runnable() {
								@Override
								public void run() {
									IDataListener listener = wListener.get();
										if ( listener != null)
											listener.onError(parseException(request,e));
								}
							});
						}
					}
				});
	}

	private static Exception parseException(IRequest request, Exception e) {
		//TODO: later: 
		//this is where all the low-level Exception is parsed to high-level Exception
		return e;
	}

	private static AutoCleanThread getStoreWorkerThread() {
		if ( storeWorkerThread == null ) {
			storeWorkerThread = new AutoCleanThread();
			storeWorkerThread.start();
		}
		return storeWorkerThread;		
	}

}
