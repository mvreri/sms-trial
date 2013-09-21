package com.vsm.radio18;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.FrameLayout;

import com.vsm.radio18.media_player.PlayerService;
import com.vsm.radio18.media_player.PlayerService.PlayerServiceBinder;
import com.vsm.radio18.ui.Topbar;

import dtd.phs.lib.utils.Helpers;

public class ActDetails extends BaseActivity {
	private static final int FRAME_LOADING = 0;
	private static final int FRAME_DATA = 1;
	private static final int FRAME_RETRY = 2;
	private Topbar topbar;
	private FrameLayout mainFrames;
	private DetailsBottomBar bottomBar;
	private boolean bound2Service;
	protected PlayerService playerService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_details);
		initTopbar();
		initSlideshowFrames();
		initBottombar();
	}

	private void initBottombar() {
		bottomBar = new DetailsBottomBar(findViewById(R.id.details_bottom));
		bottomBar.onCreate();
	}

	private void initSlideshowFrames() {
		mainFrames = (FrameLayout) findViewById(R.id.main_frames);
		Helpers.showOnlyView(mainFrames, FRAME_LOADING);
	}
	
	private void initTopbar() {
		topbar = new Topbar(findViewById(R.id.top_bar));
		topbar.onCreate();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		//loadImages(); //TODO:
		bound2Service = false;
		boolean succ = bindService(new Intent(this, PlayerService.class), playerServiceConenction, Context.BIND_AUTO_CREATE);
		if ( ! succ ) {
			Helpers.showToast(this, R.string.Error_pls_retry_later);
			finish();
		}
	}
	
	
	@Override
	protected void onPause() {
		//TODO: release images
		bound2Service = false;
		unbindService(playerServiceConenction);
		super.onPause();
	};
	
	ServiceConnection playerServiceConenction = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			bottomBar.removePlayerService();
			bound2Service = false;
			playerService = null;
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			bound2Service = true;
			PlayerServiceBinder binder = (PlayerServiceBinder) service;
			playerService = binder.getService();
			playerService.addListener(playerServiceListener);
			bottomBar.setPlayerService(playerService);
			if ( playerService.isASongSelected() ) {
				bottomBar.setSongName(playerService.getCurrentItem().getName());
			} else {
				Helpers.showToast(ActDetails.this, R.string.Error_pls_retry_later);
				finish();
				return;
			}
			
		}
	};

}
