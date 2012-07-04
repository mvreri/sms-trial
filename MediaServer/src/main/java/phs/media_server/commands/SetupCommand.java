package phs.media_server.commands;

import phs.media_server.IMediaServer;
/**
* SETUP(Full_file_path): Prepare to stream the local file</br>
* - Request: Setup###[file_path] , e.g: Setup###D:\Movies\720p.mp4 </br>
* - Respone: A string with this format </br>
*			[Success###int STREAM_SERVER_PORT###String STREAM_ID] </br>
*			Failed</br>
**/
public class SetupCommand extends Command {

	private String filePath;

	public SetupCommand(String paraStr) {
		super(paraStr);
		parseFilePath();
	}

	private void parseFilePath() {
		String rawParameter = getRawParameter(0);
		this.filePath = rawParameter;
	}

	@Override
	public void process(IMediaServer mediaServer) {
		if ( mediaServer.setup(filePath) == IMediaServer.CODE_SUCCESS ) {
			String resp = RESPONE_SUCCESS 
						+ Command.SEPERATOR + mediaServer.getServerPort()
						+ Command.SEPERATOR + mediaServer.getStreamId();
			setRespone(resp);
		} else setRespone(RESPONE_FAILED);
		
	}

}
