package phs.media_server.commands;

import phs.media_server.IMediaServer;

public class GetDurationCommand extends Command {

	public GetDurationCommand(String name,String paraStr) {
		super(name,paraStr);
	}

	@Override
	public void process(IMediaServer mediaServer) {
		int duration = mediaServer.getDuration();
		if ( duration <= 0) {
			setRespone(RESPONE_FAILED);
		} else {
			String resp = RESPONE_SUCCESS + SEPERATOR + duration;
			setRespone(resp);
		}
	}

}
