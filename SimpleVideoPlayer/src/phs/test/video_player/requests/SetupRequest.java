package phs.test.video_player.requests;

import phs.test.video_player.IMediaServerListener;
import phs.test.video_player.Logger;


public class SetupRequest extends Request {


	public SetupRequest(String reqName, String parameters, IMediaServerListener listener) {
		super(reqName, parameters, listener);
	}

	@Override
	public void processRespone(String line) {
		if ( line != null) {
			Logger.logInfo("Setup respone: " + line);
			String[] words = line.split(SEPERATOR);
			if ( words.length == 4 && isSuccess(words)){ 
				listener.onSetupRespone(RET_CODE_SUCCESS, words[2].trim(), words[3].trim());
			} else {
				listener.onSetupRespone(RET_CODE_FAILED, null, null);
			}
		} else {
			Logger.logInfo("Setup response: NULL ");
		}
	}


	@Override
	public void responseTimeout() {
		listener.onSetupRespone(RET_CODE_TIMEOUT, null, null);
	}


	@Override
	public void responseReplyException() {
		listener.onSetupRespone(RET_CODE_FAILED, null, null);
	}

}
