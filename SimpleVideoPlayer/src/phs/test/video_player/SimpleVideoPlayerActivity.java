package phs.test.video_player;

import viettel.sonph5.test.R;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SimpleVideoPlayerActivity extends Activity implements IMediaServerListener {
	protected static final String REMOTE_SERVER_VIDEO_PATH = "D:\\Movies\\720p.mp4";
	//    protected static final String MOVIE_URL = "rtsp://192.168.17.78:8554/stream";
	//    protected static final String MOVIE_URL = "rtsp://192.168.17.80:5544/stream";
	//	    protected static final String BASE_URL = "rtsp://192.168.17";
	static final String SERVER_URL = "rtsp://192.168.17.78"; 
	//    protected static final String MOVIE_URL = "http://192.168.17.78:8080/stream";
	private Button button;
	//	private EditText etIP;
	//	private EditText etPort;
	//	private EditText etId;
	private RemoteController controller;
	private String url;
	private String streamingPort;
	private String streamId;
	private Button testButton;
	private Button btAll;



	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myanlien);

		//        etIP = (EditText) findViewById(R.id.subIP);
		//        etPort = (EditText) findViewById(R.id.port);
		//        etId = (EditText) findViewById(R.id.id);

		//        button = (Button) findViewById(R.id.btPlay);
		//        button.setOnClickListener(new OnClickListener() {
		//			
		//			public void onClick(View v) {
		//		        String url = BASE_URL + "." + 
		//		        		etIP.getText().toString() + ":" + 
		//		        		etPort.getText().toString() + "/" + 
		//		        		etId.getText().toString();
		//				Log.i("PHS", url);
		//				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		//				startActivity(intent);
		//			}
		//		});
		button = (Button) findViewById(R.id.btPlay);
		controller = new RemoteController(this);
		button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				controller.setup(REMOTE_SERVER_VIDEO_PATH);
			}
		});
		
		testButton = (Button) findViewById(R.id.btTest);
		testButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent= new Intent(SimpleVideoPlayerActivity.this,VideoViewDemo.class);
				startActivity(intent);
			}
		});
		
		btAll = (Button) findViewById(R.id.btAll);
		btAll.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent= new Intent(SimpleVideoPlayerActivity.this,MediaPlayer_Video.class);
				startActivity(intent);
			}
		});

	}



	public void onSetupRespone(int errorCode, String streamPort, String streamId) {
		if ( errorCode == RemoteController.RET_CODE_SUCCESS) {
			this.streamingPort = streamPort;
			this.streamId = streamId;
			controller.start();
		} else {
			Logger.logInfo("Cannot setup the server !");
		}
	}



	public void onStartRespone(int errorCode) {
		if (errorCode == RemoteController.RET_CODE_SUCCESS) {
			Logger.logInfo("Setup server successfully !");
			url = SERVER_URL + ":" + this.streamingPort + "/" + this.streamId;
			Logger.logInfo("Streaming URL: " +url);
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));			
			startActivity(intent);
		} else {
			Logger.logInfo("Cannot start the server !");
		}
		
	}



	public void onPauseRespone(int errorCode) {}



	public void onResumeRespone(int errorCode) {
		// TODO Auto-generated method stub

	}



	public void onSeekResponse(int errorCode) {
		// TODO Auto-generated method stub

	}



	public void onStopRespone(int errorCode) {
		// TODO Auto-generated method stub

	}



	public void onElseRespone(int errorCode) {
		// TODO Auto-generated method stub

	}



	public void onGetDurationResponse(int errorCode, int duration) {
		// TODO Auto-generated method stub
		
	}
}