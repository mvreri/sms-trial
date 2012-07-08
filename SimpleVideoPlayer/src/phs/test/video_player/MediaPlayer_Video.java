package phs.test.video_player;

import java.io.IOException;

import viettel.sonph5.test.R;
import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class MediaPlayer_Video extends Activity 
implements 
OnInfoListener, OnErrorListener, 
OnVideoSizeChangedListener, 
OnBufferingUpdateListener, 
OnPreparedListener, OnCompletionListener, Callback 
{


	private static final String STREAMING_SERVER_URL = "rtsp://192.168.17.78";
	private static final String TAG = "PHS_TEST";

	
	protected static final String EXTRA_WIDTH = "extra_width";
	protected static final String EXTRA_HEIGHT = "extra_height";
	protected static final String EXTRA_MOVIE_PATH = "extra_movie_path";
	
	private MediaPlayer player;
	private SurfaceView surfaceView;
	private Display currentDisplay;
	private Button btPause;
	private View btStop;
	private RemoteController controller;
	protected boolean isPlaying;
	protected int duration;
	private View bt2Q;

	private int availWidth;
	private int availHeight;
	protected String remoteVideoPath;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.media_player);
		getInputValues();
		createSurface();
		createButtons();
	}

	@Override
	protected void onResume() {
		super.onResume();
		btPause.postDelayed(new Runnable() {
			public void run() {
				controller.setup(remoteVideoPath);
			}
		}, 300);

	}

	@Override
	protected void onPause() {
		super.onPause();
		controller.stop();
	}



	public class ControlListener implements IMediaServerListener {



		public void onSetupRespone(int errorCode, String streamPort,
				String streamId) {
			if ( errorCode == RemoteController.RET_CODE_SUCCESS) {
				try {
					player.setDataSource(STREAMING_SERVER_URL+":"+streamPort+"/"+streamId);
				} catch (IllegalArgumentException e) {
					Log.i(TAG,e.getClass().toString() + "::" + e.getMessage());
					finish();
				} catch (IllegalStateException e) {
					Log.i(TAG,e.getClass().toString() + "::" + e.getMessage());
					finish();
				} catch (IOException e) {
					Log.i(TAG,e.getClass().toString() + "::" + e.getMessage());
					finish();
				}
				controller.start();
			} else {
				Logger.logInfo("Cannot setup from server");
			}
		}

		public void onStartRespone(int errorCode) {
			if ( errorCode == RemoteController.RET_CODE_SUCCESS) {
				player.prepareAsync();
			} else {
				Logger.logInfo("Cannot start the media server ");
			}
		}

		public void onPauseRespone(int errorCode) {
			if ( errorCode == RemoteController.RET_CODE_SUCCESS) {
				Logger.logInfo("Media server is paused !");
			} else {
				Logger.logInfo("Cannot pause the media server ");
			}			
		}

		public void onResumeRespone(int errorCode) {
			if ( errorCode == RemoteController.RET_CODE_SUCCESS) {
				isPlaying = true;
				btPause.post(new Runnable() {
					public void run() {
						btPause.setText("Pause");
					}
				});
			} else {				
				Logger.logInfo("Cannot resume video on server");
			}
		}

		public void onSeekResponse(int errorCode) {

		}

		public void onStopRespone(int errorCode) {
			Logger.logInfo("STOPPED");
			if ( player != null) {
				player.stop();
				player.release();
				player = null;
				finish();
			}

		}

		public void onElseRespone(int errorCode) {
			// TODO Auto-generated method stub

		}

		public void onGetDurationResponse(int errorCode, final int duration) {
			if ( errorCode == RemoteController.RET_CODE_SUCCESS) {
				MediaPlayer_Video.this.duration = duration;
				Logger.logInfo("Duration: " + duration);
			} else {
				Logger.logInfo("Duration: error !");
			}
		}

	}

	private void createButtons() {

		controller = new RemoteController(new ControlListener());

		isPlaying = false;
		btPause = (Button) findViewById(R.id.btPause);
		btPause.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if ( isPlaying ) {
					controller.pause();
					btPause.setText("Resume");
					isPlaying = false;
				} else {
					controller.resume(-1);
				}
			}
		});

		btStop = findViewById(R.id.btStop);
		btStop.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				controller.stop();
			}
		});
		
		bt2Q = findViewById(R.id.bt2Q);
		bt2Q.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				controller.seekTo(0.50f);
			}
		});

	}

	private void createSurface() {
		surfaceView = (SurfaceView) findViewById(R.id.surface_view);
		SurfaceHolder holder = surfaceView.getHolder();
		holder.addCallback(this);
		onSurfaceCreated(holder);
	}

	private void getInputValues() {
		Intent srcIntent = getIntent();
		this.availWidth = srcIntent.getIntExtra(EXTRA_WIDTH, 640);
		this.availHeight = srcIntent.getIntExtra(EXTRA_HEIGHT, 360);
		this.remoteVideoPath = srcIntent.getStringExtra(EXTRA_MOVIE_PATH);
	}

	private void onSurfaceCreated(SurfaceHolder holder) {
		currentDisplay = getWindowManager().getDefaultDisplay();
		setupPlayer();
	}

	private void setupPlayer() {
		player = new MediaPlayer();
		player.setAudioStreamType(AudioManager.STREAM_MUSIC);
		player.setOnInfoListener(this);
		player.setOnErrorListener(this);
		player.setOnVideoSizeChangedListener(this);
		player.setOnBufferingUpdateListener(this);
		player.setScreenOnWhilePlaying(true);
		player.setOnPreparedListener(this);
		player.setOnCompletionListener(this);
	}


	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Log.i(TAG,"Surface changed");
	}

	public void surfaceCreated(SurfaceHolder holder) {
		Log.i(TAG,"Surface created");
		player.setDisplay(holder);


	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.i(TAG,"Surface destroyed");
	}


	public void onCompletion(MediaPlayer mp) {
		Log.i(TAG,"Playback is finished");
		mp.release();
		mp = null;
	}

	public void onPrepared(MediaPlayer mp) {
		Log.i(TAG,"Player is ready");
		adjustScreen(mp);
		mp.start();
		isPlaying = true;
		controller.getDuration();
	}

	private void adjustScreen(MediaPlayer mp) {
		int videoWidth = mp.getVideoWidth();
		int videoHeight = mp.getVideoHeight();
		Logger.logInfo("Avail height: " + availHeight + " -- Avail width: " + availWidth);
		if ( videoHeight > availHeight || videoWidth > availWidth) {
			float hr = ((float) videoHeight) /  (float) availHeight;
			float wr = ((float) videoWidth) /  (float) availWidth;
			float ratio = wr;
			if ( hr > wr) {
				ratio = hr;
			} 
			if ( ratio > 1) {
				videoHeight = (int) Math.ceil(((float) videoHeight) / ratio );
				videoWidth = (int) Math.ceil(((float) videoWidth) / ratio );
			}
		}
		Log.i(TAG,"Video (HxW): " + videoHeight + " x " + videoWidth);

		surfaceView.setLayoutParams(new FrameLayout.LayoutParams(videoWidth, videoHeight));
	}

	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		Log.i(TAG,"Buffering updated!");		
	}

	public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
		Log.i(TAG,"Video size changed!");		
	}

	public boolean onError(MediaPlayer mp, int what, int extra) {
		if ( what == MediaPlayer.MEDIA_ERROR_SERVER_DIED) {
			Log.i(TAG,"Error: server died!");
		} else if ( what == MediaPlayer.MEDIA_ERROR_UNKNOWN) { 
			Log.i(TAG,"Error: unknown");
		}		
		return false;
	}

	public boolean onInfo(MediaPlayer mp, int what, int extra) {
		if ( what == MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING) {
			Log.i(TAG,"Player Info: Bad interleaving !");
		} else if ( what == MediaPlayer.MEDIA_INFO_NOT_SEEKABLE) { 
			Log.i(TAG,"Player Info: Not seekable!");
		} else if ( what == MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING) { 
			Log.i(TAG,"Player Info: Track lagging");
		} else if ( what == MediaPlayer.MEDIA_INFO_UNKNOWN) { 
			Log.i(TAG,"Player error: unknown");
		} else if ( what == MediaPlayer.MEDIA_INFO_METADATA_UPDATE) {
			Log.i(TAG,"Player Info: metadata updated");
		} else {
			Log.i(TAG,"Player Info: out of error def range");
		}
		return false;
	}


}