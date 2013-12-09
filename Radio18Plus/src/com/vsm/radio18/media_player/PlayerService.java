package com.vsm.radio18.media_player;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.vsm.radio18.data.entities.ArticleItem;
import com.vsm.radio18.media_player.PlayerCore.ICoreListener;
import com.vsm.radio18.media_player.PlayerService.IPlayerListener.PlayerServiceStates;

import dtd.phs.lib.utils.Helpers;
import dtd.phs.lib.utils.Logger;

public class PlayerService extends Service {


	public interface IPlayerListener {
		public enum PlayerServiceStates {
			UNINIT, SONG_SELECTED, PLAYING, PAUSED, SONG_COMPLETED, DEAD
		};

		public void onPlayerStateChanged(PlayerServiceStates state, Object data);
	}

	// Locks
	private static final String WAKE_LOCK_TAG = "Radio_18p_wakelock";
	private static final String WIFI_LOCK_TAG = "Radio_18p_wifilock";
	private WakeLock wakeLock = null;
	private WifiLock wifiLock;
	private TelephonyManager phoneManager;

	private ArrayList<WeakReference<IPlayerListener>> wlisteners;
	private PlayerServiceStates playerState;
	
	private PlayerCore playerCore;
	private PhoneStateListener phoneListener;
	private ArticleItem currItem = null;
	//===================== BEGIN: public interface ===============================
	
	public void addListener(IPlayerListener list) {
		wlisteners.add(new WeakReference<PlayerService.IPlayerListener>(list));
	}
	
	public void playNewItem(ArticleItem item) {
		resetPlayerCore();
		this.currItem = item;
		change2State(PlayerServiceStates.SONG_SELECTED, item);
		playerCore.play(item.getStreamingURL());
	}
	

	public void pause() {
		if ( playerState == PlayerServiceStates.PLAYING ) {
			change2State(PlayerServiceStates.PAUSED, currItem);
			if ( playerCore != null ) playerCore.pause();
		} else {
			//be aware of player state before doing anything with it
			Logger.logError("Try to change state to pause from current state: " + playerState.toString());
		}
	}
	
	public void resume() {
		if ( playerState == PlayerServiceStates.PAUSED ) {
			change2State(PlayerServiceStates.PAUSED, currItem);
			if ( playerCore != null )
				playerCore.resume();
		} else if (playerState == PlayerServiceStates.SONG_COMPLETED ) {
			Logger.logInfo("Resume from STOP_N_WAIT_ state");
			if ( currItem != null ) {
				playNewItem(currItem);
			} else {
				Logger.logError("Trying to replay while currentItem is NULL");
			}
		} else {
			Logger.logError("Try to resume player from current state: " + playerState.toString());
		}
	}
	
	public ArticleItem getCurrentItem() {
		return currItem;
	}
	
	public int getCurrentPlayingTime() {
		if ( playerCore != null)
			return playerCore.getPosition();
		Logger.logError("PlayerCore is NULL");
		return 0;
	}
	
	public int getDuration() {
		if ( playerCore != null)
			return playerCore.getDuration();
		Logger.logError("PlayerCore is NULL");
		return 0;
	}
	
	
	/**
	 * 
	 * @return 
	 * 	progress of current playing song (as percentage),
	 * 	0 if no song is currently playing/paused
	 */	
	public int getProgress() {
		if ( playerCore == null ) return 0;
		switch ( playerState) {
		case PLAYING:
		case PAUSED:
			return playerCore.getProgress();
		default:
			return 0;
		}
	}
	
	public boolean isASongSelected() {
		return playerState != PlayerServiceStates.UNINIT && playerState != PlayerServiceStates.DEAD;
	}
	
	public boolean isMusicPlaying() {
		return (playerState == PlayerServiceStates.PLAYING);
	}
	
	public boolean isPaused() {
		return (playerState == PlayerServiceStates.PAUSED);
	}


	//===================== END: public interface ===============================
	
	@Override
	public void onCreate() {
		super.onCreate();
		this.wlisteners = new ArrayList<WeakReference<IPlayerListener>>();
		this.playerState = PlayerServiceStates.UNINIT;

		heldLocks();
		createPhoneListener();
	}


	@Override
	public void onDestroy() {
		change2State(PlayerServiceStates.DEAD, null);

		removePhoneListener();

		// must be released
		releasePlayerCore();
		releaseLocks();

		//good habit
		if (wlisteners != null) {
			wlisteners.clear();
			wlisteners = null;
		}

		super.onDestroy();
	}


	@Override
	public IBinder onBind(Intent arg0) {
		return new PlayerServiceBinder();
	}

	public class PlayerServiceBinder extends Binder {
		public PlayerService getService() {
			return PlayerService.this;
		}
	}
	

	private void heldLocks() {
		if (wakeLock == null) {
			PowerManager pm = (PowerManager) getApplicationContext()
					.getSystemService(Context.POWER_SERVICE);
			if (pm != null) {
				wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
						WAKE_LOCK_TAG);
				wakeLock.acquire();
			} else
				Logger.logError("Powermanager is null");
		}

		if (wifiLock == null) {
			WifiManager wm = (WifiManager) getApplicationContext()
					.getSystemService(Context.WIFI_SERVICE);
			if (wm != null) {
				wifiLock = wm.createWifiLock(WifiManager.WIFI_MODE_FULL,
						WIFI_LOCK_TAG);
				wifiLock.acquire();
			} else
				Logger.logError("Wifimanager is null");
		}
	}

	private void releaseLocks() {
		try {
			if (wifiLock != null)
				wifiLock.release();
			else
				Logger.logError("Wifi lock is NULL");
		} catch (Exception e) {
			Logger.logError(e);
		}

		try {
			if (wakeLock != null)
				wakeLock.release();
			else
				Logger.logError("Wake lock is NULL");
		} catch (Exception e) {
			Logger.logError(e);
		}
	}
	

	private void createPhoneListener() {
		phoneManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
		phoneListener = new PhoneStateListener() {
			private boolean isPausedByCallingIn = false;
			@Override
			public void onCallStateChanged(int state, String incomingNumber) {
				switch (state ){
				case TelephonyManager.CALL_STATE_OFFHOOK:
				case TelephonyManager.CALL_STATE_RINGING:
					if  ( isMusicPlaying() ) {
						pause();
						isPausedByCallingIn = true;
					}
					break;
				case TelephonyManager.CALL_STATE_IDLE:
					if ( isPausedByCallingIn ) {
						resume();
						isPausedByCallingIn = false;
					}
					break;
				}
			}
		};
		phoneManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
		
	}
	

	private void removePhoneListener() {
		phoneManager.listen(phoneListener, PhoneStateListener.LISTEN_NONE );
		phoneListener = null;
		phoneManager = null;
	}

	
	private void resetPlayerCore() {
		releasePlayerCore();
		playerCore = new PlayerCore(coreUpdater);
	}
	
	ICoreListener coreUpdater = new ICoreListener() {
		@Override
		public void onCoreUpdate(CoreStates state) {
			switch (state) {
			case IDLE:
				change2State(PlayerServiceStates.UNINIT, null);
				break;
			case INITILIZED:
				change2State(PlayerServiceStates.SONG_SELECTED, currItem);
				break;
			case PREPARING:
				//do nothing
				break;
			case PREPARED:
				//just before playing, because in core module the MediaPlayer.start() is called immediately after the player is prepared
				break;
			case STARTED:
				change2State(PlayerServiceStates.PLAYING, currItem);
				break;
			case PAUSED:
				change2State(PlayerServiceStates.PAUSED, currItem);
				break;
			case PLAYBACK_COMPLETED:
				change2State(PlayerServiceStates.SONG_COMPLETED, currItem);
				break;
			case END:
				//don't do anything here ! when it comes to END state that mean the Service is dying/dead
				break;
			case ERROR:
				change2State(PlayerServiceStates.DEAD, currItem);
				break;
			default:
				Helpers.assertCondition(false);
			}
		}
	};

	private StopTimer stopTimer;


		//TODO: later, to be added for each state
	private void change2State(PlayerServiceStates state, Object data) {
		switch (state) {
		case UNINIT:
			break;
		case SONG_SELECTED:
			Intent thisService = new Intent(getApplicationContext(),PlayerService.class);
			startService(thisService); 
			//this call makes sure the service is persistent over application life cycle (doesn't die if no UI binds to it)
			break;
		case PLAYING:
			clearStopTimer();
			break;
		case PAUSED:
			startStopTimer();
			break;
		case SONG_COMPLETED:
			startStopTimer();
			break;
		case DEAD:
			stopSelf();
			break;
		default:
			Helpers.assertCondition(false);
		}
		playerState = state;
		notifyListeners( state, data);
	}

	private void startStopTimer() {
		if ( stopTimer == null ) {
			stopTimer = new StopTimer();
			stopTimer.start();
		}
	}

	public class StopTimer extends Thread {
		private static final long WAIT_BEFORE_KILL = 5 * 60 * 1000;
		@Override
		public void run() {
			synchronized (this) {
					try {
						Logger.logInfo("StopTimer is started");
						Thread.sleep(WAIT_BEFORE_KILL);
						Logger.logInfo("StopTimer is fired");
						change2State(PlayerServiceStates.DEAD, currItem);
					} catch (InterruptedException e) {
						Logger.logInfo("StopTimer is cancelled");
						return;
					}
			}
		}

		public void clear() {
			this.interrupt();
		}
	}

	private void clearStopTimer() {
		if ( stopTimer != null ) {
			stopTimer.clear();
			stopTimer = null;
		}
	}

	private void notifyListeners(PlayerServiceStates state, Object data) {
		if ( wlisteners != null ) {
			for(WeakReference<IPlayerListener> wpl : wlisteners) {
				IPlayerListener l = wpl.get();
				if ( l != null ) {
					l.onPlayerStateChanged(state, data);
				}
			}
		}
	}
	
	private void releasePlayerCore() {
		if ( playerCore != null) {
			playerCore.release();
			playerCore = null;
		}
	}

	public synchronized void setProgress(int progress) {
		if ( playerCore != null && (playerState == PlayerServiceStates.PLAYING || playerState == PlayerServiceStates.PAUSED)) {
			playerCore.seekTo(progress);
		}
	}

}
