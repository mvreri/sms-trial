package phs.media_server.commands;

import phs.media_server.IMediaServer;
import phs.media_server.MyUtils;
import phs.media_server.MyUtils.CodeModes;


public abstract class Command {
	static final String SEPERATOR = "###";
	public static final String RESPONE_SUCCESS = "Success";
	public static final String RESPONE_FAILED = "Failed";
	
	private String[] parameters = null;
	protected String respone = null;
	
	public Command(String paraStr) {
		if ( paraStr != null )
			parameters = paraStr.split(SEPERATOR);
		if ( MyUtils.CURRENT_CODE_MODE == CodeModes.DEV) showDebugInfo();
	}
	/**
	 * 
	 * @param i 
	 * @return the i-th parameter as String
	 */
	protected String getRawParameter(int i) {
		try {
			return parameters[i];
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	public abstract void process(IMediaServer mediaServer);

	protected void setRespone(String respone) {
		this.respone = respone;
	}

	public String getRespone() {
		return this.respone;
	}

	/**
	 * Only use by debug mode
	 */
	private void showDebugInfo() {
		MyUtils.logInfo("--------------------------");
		String ctorInfo = this.getClass().getName();
		String addInfo = " empty ";
		if ( parameters != null) {
			addInfo = " -- with parameters: ";
			for(int i = 0 ; i < parameters.length ; i++) {
				addInfo += parameters[i] + "###";
			}
		}
		MyUtils.logInfo("The class: " + ctorInfo + " is being created "+ addInfo);
	}


}
