package com.vsm.radio18;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.vsm.radio18.ui.Topbar;

import dtd.phs.lib.utils.Helpers;

public class ActDetails extends BaseActivity {
	private static final int FRAME_LOADING = 0;
	private static final int FRAME_DATA = 1;
	private static final int FRAME_RETRY = 2;
	private Topbar topbar;
	private FrameLayout mainFrames;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_details);
		initTopbar();
		initSlideshowFrames();
		initBottombar();
	}

	private void initBottombar() {
		bottomBar 
	}

	private void initSlideshowFrames() {
		mainFrames = (FrameLayout) findViewById(R.id.main_frames);
		Helpers.showOnlyView(mainFrames, FRAME_LOADING);
	}
	
	private void initTopbar() {
		topbar = new Topbar(findViewById(R.id.top_bar));
		topbar.onCreate();
	}
}
