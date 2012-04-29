package hdcenter.vn;

import hdcenter.vn.utils.Helpers;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import dtd.phs.lib.ui.frames_host.FrameView;
import dtd.phs.lib.ui.frames_host.FramesActivity;

public class HDCenterActivity extends FramesActivity {

	static final int FRAME_CINEMA = 0;
	static final int FRAME_RECOMMEND = 1;
	static final int FRAME_MORE = 2;

	private static final int[] HIGHLIGHT_TAB_RES = {R.drawable.cinema , R.drawable.recommend, R.drawable.ranking};
	private static final int[] NORMAL_TAB_RES = {R.drawable.cinema_desat, R.drawable.recommend_desat, R.drawable.ranking_desat};

	private AutoCompleteTextView atSearch;
	private ImageView ivSearch;
	private ImageView ivHome;

	@Override
	public void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		createSearchModules();
	}
	
	@Override
	protected void onResume() {	
		super.onResume();
		Helpers.hideSoftKeyboard(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	private void createSearchModules() {
		atSearch = (AutoCompleteTextView) findViewById(R.id.atSearch);
		ivSearch = (ImageView) findViewById(R.id.ivSearch);
		ivSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					String s = atSearch.getText().toString().trim();
					if ( s.length() != 0 ) {
						Intent i = new Intent(getApplicationContext(),SearchMovies.class);
						i.putExtra(SearchMovies.IEXTRA_KEYWORD, s);
						startActivity(i);
					}
				} catch (Exception e) {

				}

			}
		});
		
		ivHome = (ImageView) findViewById(R.id.ivHome);
		ivHome.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Helpers.launchHomeScreen(HDCenterActivity.this);
			}
		});
		ivHome.setImageResource(R.drawable.power);
	}

	@Override
	protected int mainLayoutId() {
		return R.layout.main;
	}

	@Override
	protected void addFrames(ArrayList<FrameView> frames) {
		frames.add(new CinemaFrame(this));
		frames.add(new RecommendFrame(this));
		frames.add(new MoreFrame(this));
	}


	@Override
	protected int highlightTabResource(int i) {
		return HIGHLIGHT_TAB_RES[i];
	}

	@Override
	protected int normalTabResource(int i) {
		return NORMAL_TAB_RES[i];
	}

	@Override
	protected int[] provideTabIds() {
		return new int[] {R.id.btTabCinema,R.id.btTabRecommend,R.id.btTabMore};
	}

	@Override
	protected int getDefaultFrame() {
		return FRAME_CINEMA;
	}



}