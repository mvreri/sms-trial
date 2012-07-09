package phs.test.video_player.requests;

import phs.test.video_player.IMediaServerListener;

public class GetDurationRequest extends Request {

	public GetDurationRequest(String reqName, String parameters, IMediaServerListener listener) {
		super(reqName, parameters, listener);
	}

	@Override
	public void processRespone(String reply) {
		String[] words = reply.split(SEPERATOR);
		if (words.length==2 && isSuccess(words)) {
			listener.onGetDurationResponse(RET_CODE_SUCCESS, Integer.parseInt(words[2]));
		} else {
			listener.onGetDurationResponse(RET_CODE_FAILED, 0);
		}
	}

	@Override
	public void responseTimeout() {
		listener.onGetDurationResponse(RET_CODE_TIMEOUT, -1);
	}

	@Override
	public void responseReplyException() {
		listener.onGetDurationResponse(RET_CODE_FAILED, -1);
	}

}
