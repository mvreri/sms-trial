package phs.test.video_player.requests;

import phs.test.video_player.IMediaServerListener;

public class StopRequest extends Request {

	public StopRequest(String reqName, String parameters, IMediaServerListener listener) {
		super(reqName, parameters, listener);
	}

	@Override
	public void processRespone(String reply) {
		String[] words = reply.split(SEPERATOR);
		if ( words.length == 2 && isSuccess(words)) {
			listener.onStopRespone(RET_CODE_SUCCESS);
		} else {
			listener.onStopRespone(RET_CODE_FAILED);
		}
	}

	@Override
	public void responseTimeout() {
		listener.onStopRespone(RET_CODE_TIMEOUT);
	}

	@Override
	public void responseReplyException() {
		listener.onStopRespone(RET_CODE_FAILED);		
	}

}
