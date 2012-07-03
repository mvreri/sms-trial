package phs.media_server.commands;

import phs.media_server.IMediaServer;

public class SeekToCommand extends Command {

	private float percent;

	public SeekToCommand(String paraStr) {
		super(paraStr);
		parsePercent();
	}

	private void parsePercent() {
		String rawParameter = getRawParameter(0);
		this.percent = Float.parseFloat(rawParameter);
	}

	@Override
	public void process(IMediaServer mediaServer) {
		if ( mediaServer.seekTo(percent) == 0 ) {
			setRespone(RESPONE_SUCCESS);
		} else setRespone(RESPONE_FAILED);
	}

}
