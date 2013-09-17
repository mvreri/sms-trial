package com.vsm.radio18.media_player;

import java.io.IOException;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;

import com.vsm.radio18.media_player.PlayerCore.ICoreListener.CoreStates;

import dtd.phs.lib.utils.Logger;

/**
 * 
 * @author sonph This class is responsible for playing ONE song - it knows
 *         nothing but that song, it doesn't even care about song name,
 *         whatsoever - all it needs: the link to the song
 */
public class PlayerCore {
	// Reference:
	// http://developer.android.com/reference/android/media/MediaPlayer.html#StateDiagram
	public interface ICoreListener {
		enum CoreStates {
			IDLE, END, ERROR, INITILIZED, PREPARING, PREPARED, STARTED, PAUSED, PLAYBACK_COMPLETED
		};

		public void onCoreUpdate(CoreStates state);
	}

	private ICoreListener listener;
	private MediaPlayer androidPlayer;
	private CoreStates coreState;
	private AndroidPlayerListener androidPlayerListener;
	private int songDuration;

	// ================================= BEGIN: public interface =====================
	public PlayerCore(ICoreListener listener) {
		this.listener = listener;
		initAndroidPlayer();
	}

	public void play(String URL) {
		try {
			setSource(URL);
			updateCoreState(CoreStates.INITILIZED);

			androidPlayer.prepareAsync();
			updateCoreState(CoreStates.PREPARING);

		} catch (IllegalArgumentException e) {
			updateCoreState(CoreStates.ERROR);
			Logger.logError(e);
		} catch (IOException e) {
			updateCoreState(CoreStates.ERROR);
			Logger.logError(e);
		}
	}

	public void pause() {
		if ( androidPlayer != null ) {
			androidPlayer.pause();
			updateCoreState(CoreStates.PAUSED);
		} else {
			Logger.logError("AndroidPlayer is NULL already !");
		}
	}

	public void resume() {
		if ( androidPlayer != null ) {
			androidPlayer.start();
			updateCoreState(CoreStates.STARTED);
		}
	}

	public void release() {
		updateCoreState(CoreStates.END);
		if ( androidPlayer != null ) {
			androidPlayer.release();
			androidPlayer = null;
			listener = null;
		}
	}

	/**
	 * 
	 * @return progress of current playing song (as percentage) </br> 0 if no
	 *         song is currently playing/paused
	 */
	public int getProgress() {
		if ( songDuration <= 0 ) return 0;
		if ( coreState == CoreStates.PAUSED || coreState == CoreStates.STARTED) {
			long currentPosition = androidPlayer.getCurrentPosition();
			return (int) ((currentPosition*1000)/songDuration );
		} else {
			Logger.logInfo("Try to get currentPostion in invalid state");
			return 0;
		}

	}

	public int getPosition() {
		if ( songDuration <= 0 ) return 0;
		if ( coreState == CoreStates.PAUSED || coreState == CoreStates.STARTED ) {
			return androidPlayer.getCurrentPosition();
		} else {
			Logger.logInfo("Try to get current position in invalid state");
			return 0;
		}
	}

	/**
	 * 
	 * @return song duration in milli seconds - return 0 if the duration is unknown yet
	 */
	public int getDuration() {
		if ( songDuration <= 0) return 0;
		return songDuration;
	}

	/**
	 * 
	 * @param progress
	 * @return true if success, false otherwise (seek from invalid state)
	 */
	public boolean seekTo(int progress) {
		if ( songDuration <= 0 ) return false;
		if ( coreState == CoreStates.PAUSED || coreState == CoreStates.STARTED) {
			int position = (int) (((1.0d*progress) / 1000) * songDuration);
			androidPlayer.seekTo(position);
			return true;
		} else {
			Logger.logInfo("Try to seek in invalid state");
			return false; 
		}
	}

	// ================================= END: public interface =====================

	private void initAndroidPlayer() {
		androidPlayer = new MediaPlayer();
		addPlayerListeners();
		androidPlayer.reset();
		updateCoreState(CoreStates.IDLE);
		
	}

	protected void addPlayerListeners() {
		androidPlayerListener = new AndroidPlayerListener();
		androidPlayer.setOnBufferingUpdateListener(androidPlayerListener);
		androidPlayer.setOnCompletionListener(androidPlayerListener);
		androidPlayer.setOnErrorListener(androidPlayerListener);
		androidPlayer.setOnInfoListener(androidPlayerListener);
		androidPlayer.setOnPreparedListener(androidPlayerListener);
	}

	private void setSource(String url) throws IllegalArgumentException, IOException {
		/**
		 * This is a bug in android framework, please ref:
		 * http://code.google.com/p/android/issues/detail?id=957
		 */
		try {
			androidPlayer.setDataSource(url);
		} catch (IllegalStateException e) {
			androidPlayer.reset();
			androidPlayer.setDataSource(url);
		}

	}

	private void updateCoreState(CoreStates state) {
		coreState = state;
		if (listener != null) {
			listener.onCoreUpdate(coreState);
		} else {
			Logger.logError("Listener is NULL, the core is in state: " + state);
		}

	}

	public class AndroidPlayerListener implements OnBufferingUpdateListener,
	OnCompletionListener, OnErrorListener, OnInfoListener,
	OnPreparedListener {

		@Override
		public void onPrepared(MediaPlayer mp) {
			updateCoreState(CoreStates.PREPARED);
			mp.start();
			songDuration = mp.getDuration();
			updateCoreState(CoreStates.STARTED);
		}

		@Override
		public boolean onInfo(MediaPlayer mp, int what, int extra) {
			Logger.logInfo("Android MediaPlayer error: w = " + what + " , e = "
					+ extra);
			return false;
		}

		@Override
		public boolean onError(MediaPlayer arg0, int what, int extra) {
			Logger.logError("Android MediaPlayer error: w = " + what
					+ " , e = " + extra);
			updateCoreState(CoreStates.ERROR);
			return false;
		}

		@Override
		public void onCompletion(MediaPlayer mp) {
			updateCoreState(CoreStates.PLAYBACK_COMPLETED);
		}

		@Override
		public void onBufferingUpdate(MediaPlayer mp, int percent) {
			// wanna update buffering ?
		}
	}
}
