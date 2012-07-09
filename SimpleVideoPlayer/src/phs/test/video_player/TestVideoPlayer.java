package phs.test.video_player;

import viettel.sonph5.test.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
public class TestVideoPlayer extends Activity {
	//	protected static final String REMOTE_SERVER_VIDEO_PATH = "D:\\Movies\\Teri_Ore_mkv_aac.mkv";
	protected static final String[] VIDEO_PATHS = {
		"D:\\Movies\\720p.mp4",
		"D:\\Movies\\1080p_SNSD_Visual Dreams.mp4",
		"D:\\Movies\\1080p.aac.mp4",
		"D:\\Movies\\documentariesandyou.mp4",
		"D:\\Movies\\elements.1080p.ac3.mp4",
		"D:\\Movies\\Girls_ Generation 480.mp4",
		"D:\\Movies\\SNSD720.mp4"
	};
	static final String SERVER_URL = "rtsp://192.168.17.78"; 
//	private Button testButton;
//	private Button btAll;
	private View bigLayout;



	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
				setContentView(R.layout.myanlien);
				bigLayout = findViewById(R.id.bigLayout);
		//		testButton = (Button) findViewById(R.id.btTest);
		//		
		//		btAll = (Button) findViewById(R.id.btAll);
		//		btAll.setOnClickListener(new OnClickListener() {
		//			public void onClick(View v) {
		//				Intent intent= new Intent(TestVideoPlayer.this,MediaPlayer_Video.class);
		//				startActivity(intent);
		//			}
		//		});

		ListView listview = (ListView) findViewById(R.id.listview);
		listview.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, VIDEO_PATHS));
		listview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
				String path = VIDEO_PATHS[position];
				startVideo(path);
			}
		});
	}
	@Override
	protected void onResume() {
		super.onResume();
//		testButton.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				Intent intent= new Intent(TestVideoPlayer.this,VideoViewDemo.class);
//				startActivity(intent);
//			}
//		});
//		btAll.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				String videoPath = "";
//				startVideo(videoPath);
//			}
//
//		});
	}

	private void startVideo(String videoPath) {
		int dwidth = bigLayout.getMeasuredWidth();
		int dheight = bigLayout.getMeasuredHeight();

		Intent intent= new Intent(TestVideoPlayer.this,MediaPlayer_Video.class);

		intent.putExtra(MediaPlayer_Video.EXTRA_WIDTH, dwidth);
		intent.putExtra(MediaPlayer_Video.EXTRA_HEIGHT, dheight);
		intent.putExtra(MediaPlayer_Video.EXTRA_MOVIE_PATH, videoPath);

		startActivity(intent);
	}

}