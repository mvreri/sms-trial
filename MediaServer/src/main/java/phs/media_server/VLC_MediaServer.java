package phs.media_server;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.headless.HeadlessMediaPlayer;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import uk.co.caprica.vlcj.runtime.x.LibXUtil;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

public class VLC_MediaServer implements IMediaServer {

	public enum PlayerStates { ZERO,INITILIZED,STREAMING,PAUSED };

	private static final String VLC_APP_PATH = "c:/Program Files/VideoLAN/VLC/";
	private static final int CODE_SUCCESS = 0;
	private static final int CODE_INVALID_STATE = 1;

	private static final String STREAM_ID = "stream";

	private String filePath;
	private HeadlessMediaPlayer mediaPlayer;
	private PlayerStates currentState;
	private int serverPort;

	@Override
	public int setup(String fullFilePath) {
		if ( currentState == PlayerStates.ZERO) {
			loadVLCLib();
			this.filePath = fullFilePath;
			currentState = PlayerStates.INITILIZED;
			this.serverPort = MyUtils.findFreePort();
			return this.serverPort;
		} else return -1;
	}

	private void loadVLCLib() {
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), getVLCPath());
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
		LibXUtil.initialise();
	}


	/**
	 * Get the VLC app file path from the devopling enviroment
	 * @return the path to VLC folder
	 */	
	private String getVLCPath() {
		//TODO: automatic scan for the path of VLC
		return VLC_APP_PATH;
	}

	@Override
	public int start() {
		if ( currentState == PlayerStates.INITILIZED ) {
			MediaPlayerFactory fact = new MediaPlayerFactory();
			mediaPlayer = fact.newHeadlessMediaPlayer();
			mediaPlayer.playMedia(
					this.filePath, 
					createBaseURL(getServerPort(),getStreamId()),
					":no-sout-rtp-sap", 
					":no-sout-standard-sap", 
					":sout-all", 
					":ttl=228",
					":sout-keep"					
					);
			currentState = PlayerStates.STREAMING;
			return CODE_SUCCESS;
		} return CODE_INVALID_STATE;
	}

	@Override
	public String getStreamId() {
		return STREAM_ID;
	}

	@Override
	public int getServerPort() {
		return serverPort;
	}

	private static String createBaseURL(int serverPort, String id) {
		StringBuilder sb = new StringBuilder(60);
		sb.append(":sout=#rtp{sdp=rtsp://"); 
		sb.append(':');
		sb.append(serverPort);
		sb.append('/');
		sb.append(id);
		sb.append("}");
		return sb.toString();
	}

	@Override
	public int stop() {
		if ( currentState != PlayerStates.ZERO) {
			mediaPlayer.stop();
			mediaPlayer = null;
			currentState = PlayerStates.ZERO;
			return CODE_SUCCESS;
		} else return CODE_INVALID_STATE;
	}

	@Override
	public int pause() {
		if ( currentState == PlayerStates.STREAMING) {
			mediaPlayer.pause();
			currentState = PlayerStates.PAUSED;
			return CODE_SUCCESS;
		} else return CODE_INVALID_STATE;
	}

	@Override
	public int resume() {
		if ( currentState == PlayerStates.PAUSED) {
			mediaPlayer.pause();
			currentState = PlayerStates.STREAMING;
			return CODE_INVALID_STATE;
		} return CODE_INVALID_STATE;
	}

	@Override
	public int seekTo(float percent) {
		if ( 
				currentState == PlayerStates.PAUSED 
				|| currentState == PlayerStates.STREAMING
				&& percent >= 0 && percent <= 1
			)
		{
			mediaPlayer.setPosition(percent);
			return CODE_SUCCESS; 
		} else return CODE_INVALID_STATE;
	}

}
