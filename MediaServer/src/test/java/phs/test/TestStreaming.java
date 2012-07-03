package phs.test;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.headless.HeadlessMediaPlayer;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import uk.co.caprica.vlcj.runtime.x.LibXUtil;
import uk.co.caprica.vlcj.test.VlcjTest;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

public class TestStreaming extends VlcjTest{


	private static final String ID = "stream";
	private static final String IP_ADDR = "127.0.0.1";
	private static final int PORT = 8554;
	private static final int COMMAND_PORT = 17584;

	public static void main(String[] args) throws Exception {
		TestStreaming test = new TestStreaming();
		test.begin();		
		Thread.currentThread().join();
	}

	private void begin() throws InterruptedException {
		startStreaming();
	}

	private void startStreaming() {
		StreamingServer server = new StreamingServer();
		server.start();
	}

	private static String formatRtspStream(String serverAddress, int serverPort, String id) {
		StringBuilder sb = new StringBuilder(60);
		sb.append(":sout=#rtp{sdp=rtsp://"); 
		//		sb.append(serverAddress);
		sb.append(':');
		sb.append(serverPort);
		sb.append('/');
		sb.append(id);
		sb.append("}");
		//		sb.append(":sout-all :sout-keep");
		return sb.toString();
	}
	
	public class StreamingServer extends Thread {
		@Override
		public void run() {
			NativeLibrary.addSearchPath( RuntimeUtil.getLibVlcLibraryName(), "c:/Program Files/VideoLAN/VLC/");
			Native.loadLibrary( RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
			LibXUtil.initialise();
			String media = "D:\\Movies\\720p.mp4";
//			String options = formatRtspStream(IP_ADDR, PORT, ID);
//			System.out.println("Streaming '" + media + "' to '" + options + "'");

			MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory("");
			HeadlessMediaPlayer mediaPlayer = mediaPlayerFactory.newHeadlessMediaPlayer();
//			mediaPlayer.playMedia(media,
//					options,
//					":no-sout-rtp-sap", 
//					":no-sout-standard-sap", 
//					":sout-all", 
//					":sout-keep"
//					);
//			
			String [] options ={ ":sout =#transcode{vcodec=h264,vb=56,fps=12,scale=0.5,width=176,height=144,acodec=mp4a,ab=24,channels=1,samplerate=44100} :rtp{sdp=rtsp://:5544/ok.mp4} :no-sout-rtp-sap :no-sout-standard-sap :ttl=1 :sout-keep"} ;
			mediaPlayer.playMedia(media, options);
			
		}
	}

}
