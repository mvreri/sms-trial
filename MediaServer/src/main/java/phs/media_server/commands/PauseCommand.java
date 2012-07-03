package phs.media_server.commands;

import phs.media_server.IMediaServer;

public class PauseCommand extends Command {

	public PauseCommand(String paraStr) {
		super(paraStr);
	}

	@Override
	public void process(IMediaServer mediaServer) {
		if ( mediaServer.pause() == 0 ) { 
			setRespone(Command.RESPONE_SUCCESS);
		} else setRespone(Command.RESPONE_FAILED);
	}

}
