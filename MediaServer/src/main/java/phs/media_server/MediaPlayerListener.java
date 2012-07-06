package phs.media_server;

import com.sun.org.apache.bcel.internal.generic.GETSTATIC;

import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventListener;
import uk.co.caprica.vlcj.player.VideoMetaData;

public class MediaPlayerListener implements MediaPlayerEventListener {

	@Override
	public void mediaChanged(MediaPlayer mediaPlayer) {
	}

	@Override
	public void opening(MediaPlayer mediaPlayer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void buffering(MediaPlayer mediaPlayer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void playing(MediaPlayer mediaPlayer) {
		Logger.logInfo("Media length(Playing): " + mediaPlayer.getLength());
	}

	@Override
	public void paused(MediaPlayer mediaPlayer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void stopped(MediaPlayer mediaPlayer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void forward(MediaPlayer mediaPlayer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void backward(MediaPlayer mediaPlayer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void finished(MediaPlayer mediaPlayer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void timeChanged(MediaPlayer mediaPlayer, long newTime) {
		// TODO Auto-generated method stub

	}

	@Override
	public void positionChanged(MediaPlayer mediaPlayer, float newPosition) {
		// TODO Auto-generated method stub

	}

	@Override
	public void seekableChanged(MediaPlayer mediaPlayer, int newSeekable) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pausableChanged(MediaPlayer mediaPlayer, int newSeekable) {
		// TODO Auto-generated method stub

	}

	@Override
	public void titleChanged(MediaPlayer mediaPlayer, int newSeekable) {
		// TODO Auto-generated method stub

	}

	@Override
	public void snapshotTaken(MediaPlayer mediaPlayer, String filename) {
		// TODO Auto-generated method stub

	}

	@Override
	public void lengthChanged(MediaPlayer mediaPlayer, long newLength) {
		// TODO Auto-generated method stub

	}

	@Override
	public void error(MediaPlayer mediaPlayer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void metaDataAvailable(MediaPlayer mediaPlayer,
			VideoMetaData videoMetaData) {
		Logger.logInfo("Media length(Metadata): " + mediaPlayer.getLength());
	}

}
