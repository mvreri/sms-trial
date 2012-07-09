package phs.test.video_player.requests;

import phs.test.video_player.IMediaServerListener;

public class SeekToRequest extends Request {

	public SeekToRequest(String reqName, String parameters, IMediaServerListener listener) {
		super(reqName, parameters, listener);
	}

	@Override
	public void processRespone(String reply) {
		String[] words = reply.split(SEPERATOR);
		if (words.length==2 && isSuccess(words)) {
			listener.onSeekResponse(RET_CODE_SUCCESS);
		} else {
			listener.onSeekResponse(RET_CODE_FAILED);
		}		
	}

	@Override
	public void responseTimeout() {
		listener.onSeekResponse(RET_CODE_TIMEOUT);
	}

	@Override
	public void responseReplyException() {
		listener.onSeekResponse(RET_CODE_FAILED);
	}

}
