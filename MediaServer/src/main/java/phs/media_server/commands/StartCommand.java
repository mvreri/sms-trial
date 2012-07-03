package phs.media_server.commands;

import phs.media_server.IMediaServer;

public class StartCommand extends Command {

	public StartCommand(String paraStr) {
		super(paraStr);
	}

	@Override
	public void process(IMediaServer mediaServer) {
		if ( mediaServer.start() == 0 ) {
			setRespone(RESPONE_SUCCESS);
		} else setRespone(RESPONE_FAILED);
	}

}
