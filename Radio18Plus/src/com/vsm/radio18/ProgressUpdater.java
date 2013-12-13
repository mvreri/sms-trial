package com.vsm.radio18;

import com.vsm.radio18.media_player.PlayerService;

public class ProgressUpdater extends Thread {
	public interface IProgressListener {
		void onProgressChanged(int progress, int currentTime, int duration);
	}

	private volatile boolean stopped;
	private IProgressListener progressListener;
	private PlayerService playerService;

	public ProgressUpdater(IProgressListener listener) {
		this.stopped = false;
		this.progressListener = listener;
	}

	public synchronized void setPlayerService(PlayerService playerService) {
		this.playerService = playerService;
	}

	public synchronized void removePlayerService() {
		this.playerService = null;
	}

	@Override
	public void run() {
		while (!stopped) {
			synchronized (this) {
				if (playerService != null) {
					if (playerService.isMusicPlaying()
							|| playerService.isPaused()) {
						int duration = playerService.getDuration();
						int currentTime = playerService.getCurrentPlayingTime();
						int progress = playerService.getProgress();
						if (progressListener != null)
							progressListener.onProgressChanged(progress,
									currentTime, duration);
					}
				}
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// do nothing
			}
		}
	}

	public synchronized void stopUpdate() {
		stopped = true;
	}

}
