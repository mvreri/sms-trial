package phs.test.video_player;

import java.io.IOException;

import phs.test.video_player.requests.Request;
import phs.test.video_player.requests.RequestFactory;
import phs.test.video_player.requests.RequestProcessor;

public class RemoteController {

	private IMediaServerListener listener;
	private RequestProcessor requestProcessor;

	public RemoteController(IMediaServerListener listener) {
		this.listener = listener;
		createCommunicator();
	}

	private void createCommunicator() {
		try {
			requestProcessor = new RequestProcessor();
		} catch (IOException e) {
			Logger.logError(e);
		} 
	}


	public void setup(String remoteVideoPath) {
		Request request = RequestFactory.createRequest("setup",remoteVideoPath,listener);
		requestProcessor.addRequest(request);
	}


	public void start() {
		Request request = RequestFactory.createRequest("start", null, listener);
		requestProcessor.addRequest(request);
	}


	public void stop() {
		Request request = RequestFactory.createRequest("stop", null, listener);
		requestProcessor.addRequest(request);		
	}


	public void pause() {
		Request request = RequestFactory.createRequest("pause", null, listener);
		requestProcessor.addRequest(request);
	}

	public void resume(float percent) {
		Request request = RequestFactory.createRequest("resume", null, listener);
		requestProcessor.addRequest(request);
	}
	
	public void seekTo(float next) {
		Request request = RequestFactory.createRequest("seekto", String.valueOf(next), listener);
		requestProcessor.addRequest(request);
	}
	
	public void getDuration() {
		Request request = RequestFactory.createRequest("getduration", null, listener);
		requestProcessor.addRequest(request);
	}

	/**
	 * Release resources, this method must be called when media application want to close the session !
	 */
	public void destroy() {
		requestProcessor.stop();
	}




}
