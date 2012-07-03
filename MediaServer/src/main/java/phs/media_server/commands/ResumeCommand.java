package phs.media_server.commands;

import phs.media_server.IMediaServer;

public class ResumeCommand extends Command {

	public ResumeCommand(String paraStr) {
		super(paraStr);
	}

	@Override
	public void process(IMediaServer mediaServer) {
		if ( mediaServer.resume() == 0 ) {
			setRespone(RESPONE_SUCCESS);
		} else setRespone(RESPONE_FAILED);
	}

}
