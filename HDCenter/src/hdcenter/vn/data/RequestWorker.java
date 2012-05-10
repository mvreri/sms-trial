package hdcenter.vn.data;

import hdcenter.vn.data.requests.IRequest;
import hdcenter.vn.data.requests.Request;

import java.io.IOException;

import org.json.JSONException;

import android.os.Handler;

public class RequestWorker {

	private volatile static AutoCleanThread workerThread = null;

	public static void add(
			final IRequest request,
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

				} catch (final Exception e) {
					handler.post(new Runnable() {
						@Override
						public void run() {
							listener.onRequestError(parseException(request,e));
						}
					});
				}
			}
		});
	}
	
	private static Exception parseException(IRequest request, Exception e) {
		Exception res = e;
		if (request instanceof Request) {
			if ( e instanceof IOException) {
				res = new ConnectionException();
			} else if (e instanceof JSONException) {
				res = new FalseReturnDataException();
			}								
		}  
		return res;
	}

	private static  AutoCleanThread getWorkerThread() {
		if ( workerThread == null ) {
			workerThread = new AutoCleanThread();
			workerThread.start();
		}
		return workerThread;
	}

}
