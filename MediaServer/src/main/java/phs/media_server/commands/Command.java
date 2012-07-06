package phs.media_server.commands;

import phs.media_server.IMediaServer;
import phs.media_server.Logger;
import phs.media_server.Logger.CodeModes;


public abstract class Command {
	static final String SEPERATOR = "###";
	public static final String RESPONE_SUCCESS = "Success";
	public static final String RESPONE_FAILED = "Failed";
	public static final String UNKNOWN_COMMAND = "Unknown command";
	
	private String[] parameters = null;
	protected String respone = null;
	
	public Command(String paraStr) {
		if ( paraStr != null )
			parameters = paraStr.split(SEPERATOR);
		if ( Logger.CURRENT_CODE_MODE == CodeModes.DEV) showDebugInfo();
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
		Logger.logInfo("--------------------------");
		String ctorInfo = this.getClass().getName();
		String addInfo = " empty ";
		if ( parameters != null) {
			addInfo = " -- with parameters: ";
			for(int i = 0 ; i < parameters.length ; i++) {
				addInfo += parameters[i] + "###";
			}
		}
		Logger.logInfo("The class: " + ctorInfo + " is being created "+ addInfo);
	}


}
