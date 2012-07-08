package phs.test.video_player.requests;

import phs.test.video_player.Helpers;
import phs.test.video_player.IMediaServerListener;



public abstract class Request {

	public static final String SEPERATOR = "###";
	public static final String RESPONE_SUCCESS = "success";
	public static final String RESPONE_FAILED = "failed";

	//Return code for the listener
	static final int RET_CODE_SUCCESS = 0;
	static final int RET_CODE_FAILED = 1;
	static final int RET_CODE_TIMEOUT = 2;

	protected String requestName;
	private String[] rawParameters = null;
	protected IMediaServerListener listener = null;
	
	public Request(String reqName, String parameters, IMediaServerListener listener) {
		this.requestName = reqName.toLowerCase();
		if ( parameters != null )
			rawParameters = parameters.split(SEPERATOR);
		this.listener = listener;
	}
	
	public String getRawParameter(int i) {
		Helpers.verify( rawParameters != null);
		Helpers.verify( i < rawParameters.length );
		return rawParameters[i];
	}
	
	
	public String reqString() {
		return this.requestName + buildParameters();
	}

	abstract public void processRespone(String reply);
	
	/**
	 * Handle the exception when request times out 
	 */
	abstract public void responseTimeout();
	
	/**
	 * Handle the exception raised while the client waiting for reply from server
	 */
	abstract public void responseReplyException();

	protected  String buildParameters() {
		if ( rawParameters != null) {
			StringBuilder builder = new StringBuilder();
			for(int i = 0 ; i < rawParameters.length ; i++) {
				builder.append(getRawParameter(i));
				if ( i < rawParameters.length - 1 ) builder.append(SEPERATOR);
			}
			return builder.toString();
		} else return "";
	}
	

	
	

}
