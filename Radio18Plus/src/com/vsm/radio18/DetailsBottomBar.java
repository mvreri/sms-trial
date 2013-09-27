package com.vsm.radio18;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.vsm.radio18.media_player.PlayerService;

import dtd.phs.lib.utils.Helpers;

public class DetailsBottomBar {
	public static final int UNINIT = 0;
	private View root;
	//private SeekBar seekbar;
	private ImageView ivPlayPause;
	private TextView tvTitle;
	private TextView tvCurTime;
	private TextView tvDuration;
	private int curTime;
	private int duration = UNINIT;
	private PlayerService playerService;

	public DetailsBottomBar(View root) {
		this.root = root;
	}
	
	private View findViewById(int id) {
		return root.findViewById(id);
	}

	public void onCreate() {
		//seekbar = (SeekBar) findViewById(R.id.seekbar);
		ivPlayPause = (ImageView) findViewById(R.id.ivPlayPause);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvCurTime = (TextView) findViewById(R.id.tvCurTime);
		tvDuration = (TextView) findViewById(R.id.tvDuration);
		
		OnClickListener onPlayPauseClick = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if ( playerService != null) {
					if ( playerService.isMusicPlaying() ) {
						playerService.pause();
						ivPlayPause.setImageResource(R.drawable.ic_play);
					} else if ( playerService.isPaused() ) {
						playerService.resume();
						ivPlayPause.setImageResource(R.drawable.ic_pause);
					}
				}
			}
		};
		ivPlayPause.setOnClickListener(onPlayPauseClick);
	}

	public void onResume() {
		//TODO: listen for time changed
	}


	public void onPause() {
		//TODO: stop listening for time changed
	}

	public void setPlayerService(PlayerService playerService) {
		this.playerService = playerService;
	}

	public void removePlayerService() {
		this.playerService = null;
	}

	public void setSongName(String name) {
		this.tvTitle.setText(name);
	}

	public void onTimeChanged(int currentTime, int duration) {
		
		if ( this.duration == UNINIT ) {
			this.duration = duration;
			tvDuration.setText("" + Helpers.convert2ReadalbeTime(duration));
		} 
		tvCurTime.setText(Helpers.convert2ReadalbeTime(currentTime));
	}

}
