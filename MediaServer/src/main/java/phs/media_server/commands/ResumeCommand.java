package phs.media_server.commands;

import phs.media_server.IMediaServer;

public class ResumeCommand extends Command {

	private float percent;

	public ResumeCommand(String paraStr) {
		super(paraStr);
		parsePercent();
	}

	private void parsePercent() {
		String para = getRawParameter(0);
		this.percent = Float.parseFloat(para);
	}

	@Override
	public void process(IMediaServer mediaServer) {
		if ( mediaServer.resume(percent) == 0 ) {
			setRespone(RESPONE_SUCCESS);
		} else setRespone(RESPONE_FAILED);
	}

}
