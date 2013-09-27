package com.vsm.radio18.ui;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vsm.radio18.ActDetails;
import com.vsm.radio18.R;
import com.vsm.radio18.media_player.PlayerService;

import dtd.phs.lib.utils.Helpers;

public class BottomControllers {

	public static final int UNINIT = 0;
	private View root;
	private TextView tvName;
	private TextView tvTime;
	private Activity act;
	private PlayerService playerService;
	private ProgressBar progressBar;
	public int duration = UNINIT;
	private ImageView ivPlayPause;
	private volatile ProgressUpdater pUpdater = null;
	// the lifecycle of this updater: onResume -> onPause
	private int progress;
	private int curTime;
	private int articleId;

	public BottomControllers(Activity act,View view) {
		this.root = view;
		this.act = act;
	}

	public void onCreate() {
		tvName = (TextView) root.findViewById(R.id.tvArticleName);
		tvTime = (TextView) root.findViewById(R.id.tvTime);
		progressBar = (ProgressBar) root.findViewById(R.id.progressBar);
		ivPlayPause = (ImageView) root.findViewById(R.id.ivPause);
		disable();
		OnClickListener onLayoutClick = new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(act,ActDetails.class);
				act.startActivity(i);
			}
		};
		root.setOnClickListener(onLayoutClick);
	}

	public void onResume() {
		startProgressUpdater();
	}

	public void onPause() {
		stopProgressUpdater();
	}

	public void disable() {
		this.root.setVisibility(View.GONE);
	}

	public void enable() {
		this.root.setVisibility(View.VISIBLE);
	}

	protected void startProgressUpdater() {

		if (pUpdater == null) {
			pUpdater = new ProgressUpdater();
			pUpdater.start();
		}

	}

	protected void stopProgressUpdater() {

		if (pUpdater != null) {
			pUpdater.stopUpdate();
			pUpdater = null;
		}

	}

	public class ProgressUpdater extends Thread {
		private volatile boolean stopped;

		public ProgressUpdater() {
			super("ProgressUpdater");
			stopped = false;
		}

		public void stopUpdate() {
			stopped = true;
			interrupt();
		}

		@Override
		public void run() {
			while (!stopped) {
				if (root.getVisibility() == View.VISIBLE
						&& playerService != null) {
					updateDuration();
					updateProgress();
					postTime();
				}
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// do nothing
				}
			}
		}

		private void postTime() {
			root.post(new Runnable() {
				@Override
				public void run() {
					String cTimeString = Helpers.convert2ReadalbeTime(curTime);
					String durationString = Helpers
							.convert2ReadalbeTime(duration);
					tvTime.setText(cTimeString + " / " + durationString);
					progressBar.setProgress(progress);
				}
			});
		}

		private void updateProgress() {
			if (playerService.isPaused() || playerService.isMusicPlaying()) {
				progress = playerService.getProgress();
				curTime = playerService.getCurrentPlayingTime();
			}

		}

		private void updateDuration() {
			if (duration == UNINIT
					&& (playerService.isMusicPlaying() || playerService
							.isPaused()))
				duration = playerService.getDuration();
		}

	}

	public void resetSongName(String name) {
		tvName.setText(name);
		duration = UNINIT;
		progress = 0;
		curTime = 0;
		updatePlayButtonState();
		this.enable();
	}

	OnClickListener onPausePressed = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (playerService != null && playerService.isMusicPlaying()) {
				playerService.pause();
			}
		}
	};
	OnClickListener onPlayPressed = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (playerService != null) {
				playerService.resume();
			}
		}
	};

	protected void updatePlayButtonState() {
		if (playerService != null) {
			if (playerService.isMusicPlaying()) {
				ivPlayPause.setImageResource(R.drawable.ic_pause);
				ivPlayPause.setOnClickListener(onPausePressed);
			} else if (playerService.isPaused()) {
				ivPlayPause.setImageResource(R.drawable.ic_play);
				ivPlayPause.setOnClickListener(onPlayPressed);
			} else if ( playerService.isASongSelected() ) {
			}

			// TODO: later - what if end of song & wanna replay ?
		} else {
			ivPlayPause.setImageResource(R.drawable.ic_play);
			ivPlayPause.setOnClickListener(null);
		}
	}

	public void removePlayerService() {
		this.playerService = null;
	}

	public void setPlayerService(PlayerService playerService) {
		this.playerService = playerService;
	}

	public void change2PlayingState() {
		updatePlayButtonState();
	}

	public void change2PausedState() {
		updatePlayButtonState();
	}

	public void change2StopNWaitState() {
		if ( playerService != null && playerService.isASongSelected() ) {
			resetSongName(playerService.getCurrentItem().getName());
			ivPlayPause.setImageResource(R.drawable.ic_play);
			ivPlayPause.setOnClickListener(onPlayPressed);
		} else {
			disable();
		}
	}

}
