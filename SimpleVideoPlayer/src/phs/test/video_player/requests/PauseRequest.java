package phs.test.video_player.requests;

import phs.test.video_player.IMediaServerListener;

public class PauseRequest extends Request {

	public PauseRequest(
			String reqName, 
			String parameters,
			IMediaServerListener listener) {
		
		super(reqName, parameters, listener);
	}

	@Override
	public void processRespone(String reply) {
		String[] words = reply.split(SEPERATOR);
		if (words.length==2 &&  isSuccess(words) ) {
			listener.onPauseRespone(RET_CODE_SUCCESS);
		} else {
			listener.onPauseRespone(RET_CODE_FAILED);
		}		
	}


	@Override
	public void responseTimeout() {
		listener.onPauseRespone(RET_CODE_TIMEOUT);
	}

	@Override
	public void responseReplyException() {
		listener.onPauseRespone(RET_CODE_FAILED);

	}

}
