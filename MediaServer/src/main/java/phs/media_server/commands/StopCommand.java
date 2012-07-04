package phs.media_server.commands;

import phs.media_server.IMediaServer;

public class StopCommand extends Command {

	public StopCommand(String paraStr) {
		super(paraStr);
	}

	@Override
	public void process(IMediaServer mediaServer) {
		if ( mediaServer.stop() == 0 ) {
			setRespone(RESPONE_SUCCESS);
		} else setRespone(RESPONE_FAILED);
	}


}
