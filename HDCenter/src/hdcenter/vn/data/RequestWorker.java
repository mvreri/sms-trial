package hdcenter.vn.data;

import hdcenter.vn.data.requests.Request;

import java.io.IOException;

import org.json.JSONException;

import android.os.Handler;

public class RequestWorker {

	private volatile static AutoCleanThread workerThread = null;

	public static void add(
			final Request request,
			final IRequestListener listener,
			final Handler handler) {
		getWorkerThread().addJob(new Runnable() {
			@Override
			public void run() {
				try {
					final Object data = request.requestData();
					handler.post(new Runnable() {
						@Override
						public void run() {
							listener.onRequestSuccess(data);
						}
					});
					
				} catch (IOException e) {
					handler.post(new Runnable() {
						@Override
						public void run() {
							listener.onRequestError( new ConnectionException());
						}
					});
				} catch (JSONException e) {
					handler.post(new Runnable() {
						@Override
						public void run() {
							listener.onRequestError( new FalseReturnDataException());
						}
					});
					
				}
			}
		});
	}

	private static  AutoCleanThread getWorkerThread() {
		if ( workerThread == null ) {
			workerThread = new AutoCleanThread();
			workerThread.start();
		}
		return workerThread;
	}

}
