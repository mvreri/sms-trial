package phs.test.video_player.requests;

import phs.test.video_player.IMediaServerListener;

public class ResumeRequest extends Request {

	public ResumeRequest(String reqName, String parameters, IMediaServerListener listener) {
		super(reqName, parameters, listener);
	}

	@Override
	public void processRespone(String reply) {
		String[] words = reply.split(SEPERATOR);
		if (words.length==2 && isSuccess(words)) {
			listener.onResumeRespone(RET_CODE_SUCCESS);
		} else {
			listener.onResumeRespone(RET_CODE_FAILED);
		}
	}

	@Override
	public void responseTimeout() {
		listener.onResumeRespone(RET_CODE_TIMEOUT);
	}

	@Override
	public void responseReplyException() {
		listener.onResumeRespone(RET_CODE_FAILED);
	}

}
