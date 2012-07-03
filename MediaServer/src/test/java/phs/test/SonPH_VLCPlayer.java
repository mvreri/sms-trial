	package phs.test;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.EmbeddedMediaListPlayerComponent;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

public class SonPH_VLCPlayer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//TODO: how to get the path from the system ? note hardcore like this
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "C:\\Program Files\\VideoLAN\\VLC");
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new SonPH_VLCPlayer();
			}
		});
	}

	private EmbeddedMediaListPlayerComponent mediaPlayerComponent;

	public SonPH_VLCPlayer() {
		JFrame frame = new JFrame("VLC experiment");
		
		mediaPlayerComponent = new EmbeddedMediaListPlayerComponent();
		frame.setContentPane(mediaPlayerComponent);
		
		frame.setLocation(100,100);
		frame.setSize(1280,720);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		mediaPlayerComponent.getMediaPlayer().playMedia("D:\\Movies\\720p.aac.mp4");
		
	}
	

}
