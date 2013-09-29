package com.vsm.radio18;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.vsm.radio18.data.entities.ArticleItem;
import com.vsm.radio18.media_player.PlayerService;
import com.vsm.radio18.media_player.PlayerService.IPlayerListener;
import com.vsm.radio18.media_player.PlayerService.PlayerServiceBinder;
import com.vsm.radio18.ui.BottomControllers;

import dtd.phs.lib.utils.Logger;

public class ActivityWithBottomBar extends BaseActivity {
	private BottomControllers bottomControllers;
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		bindService(new Intent(this,PlayerService.class), playerServiceConnection, Context.BIND_AUTO_CREATE);
		bottomControllers.onResume();
	}
	
	@Override
	protected void onPause() {
		unboundPlayerService();
		bottomControllers.onPause();
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	protected void unboundPlayerService() {
		bound2Service = false;
		unbindService(playerServiceConnection);
	}
	
	protected boolean bound2Service = false;
	protected PlayerService playerService = null;
	private ServiceConnection playerServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			Logger.logError("Player  service is disconnected: Weird");
			bottomControllers.removePlayerService();
			bound2Service = false;
			playerService = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			bound2Service = true;
			PlayerServiceBinder binder = (PlayerServiceBinder) service;
			playerService = binder.getService();
			playerService.addListener(playerServiceListener);
			bottomControllers.setPlayerService(playerService);
			if ( playerService.isASongSelected() ) {
				bottomControllers.enable();
				bottomControllers.resetSongName(playerService.getCurrentItem().getName());
			}
			Logger.logInfo("PlayerService is connected ");
		}
		IPlayerListener playerServiceListener = new IPlayerListener() {
			@Override
			public void onPlayerStateChanged(PlayerServiceStates state, Object data) {
				ArticleItem item = null;
				switch (state) {
				case UNINIT:
					break;
				case SONG_SELECTED:
					item = playerService.getCurrentItem();
					if ( item != null ) {
						bottomControllers.resetSongName(item.getName());
					} else Logger.logError("data is NULL here");
					break;
				case PLAYING:
					bottomControllers.change2PlayingState();
					break;
				case PAUSED:
					bottomControllers.change2PausedState();
					break;
				case SONG_COMPLETED:
					bottomControllers.change2StopNWaitState();
					break;
				case DEAD:
					break;
				}
			}
		};
	};
	
	protected void initBottomControllers() {
		bottomControllers = new BottomControllers(this,findViewById(R.id.bottom_controllers));
		bottomControllers.onCreate();
	}
	
}
