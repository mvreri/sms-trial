package phs.test.video_player;

import viettel.sonph5.test.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TestVideoPlayer extends Activity {
	protected static final String REMOTE_SERVER_VIDEO_PATH = "D:\\Movies\\720p.mp4";
	static final String SERVER_URL = "rtsp://192.168.17.78"; 
	private Button testButton;
	private Button btAll;
	private View bigLayout;



	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myanlien);
		bigLayout = findViewById(R.id.bigLayout);
		testButton = (Button) findViewById(R.id.btTest);
		
		btAll = (Button) findViewById(R.id.btAll);
		btAll.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent= new Intent(TestVideoPlayer.this,MediaPlayer_Video.class);
				startActivity(intent);
			}
		});

	}
	
	@Override
	protected void onResume() {
		super.onResume();
		testButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent= new Intent(TestVideoPlayer.this,VideoViewDemo.class);
				startActivity(intent);
			}
		});
		btAll.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				int dwidth = bigLayout.getMeasuredWidth();
				int dheight = bigLayout.getMeasuredHeight();
				
				Intent intent= new Intent(TestVideoPlayer.this,MediaPlayer_Video.class);
				
				intent.putExtra(MediaPlayer_Video.EXTRA_WIDTH, dwidth);
				intent.putExtra(MediaPlayer_Video.EXTRA_HEIGHT, dheight);
				intent.putExtra(MediaPlayer_Video.EXTRA_MOVIE_PATH, REMOTE_SERVER_VIDEO_PATH);
				
				startActivity(intent);
			}
		});
	}
}