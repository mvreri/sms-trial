package phs.test.video_player.requests;

import phs.test.video_player.IMediaServerListener;
import phs.test.video_player.Logger;

public class StartRequest extends Request {

	public StartRequest(String reqName, String parameters, IMediaServerListener listener) {
		super(reqName, parameters, listener);
	}

	@Override
	public void processRespone(String reply) {
		Logger.logInfo("Start respone: " + reply);
		String[] words = reply.split(SEPERATOR);
		if ( words.length == 2 
				&& words[0].toLowerCase().equals("start")
				&& words[1].toLowerCase().equals(RESPONE_SUCCESS)
				) {
			listener.onStartRespone(RET_CODE_SUCCESS);
		} else listener.onStartRespone(RET_CODE_FAILED);
	}

	@Override
	public void responseTimeout() {
		listener.onStartRespone(RET_CODE_TIMEOUT);
	}

	@Override
	public void responseReplyException() {
		listener.onStartRespone(RET_CODE_FAILED);
	}

}
