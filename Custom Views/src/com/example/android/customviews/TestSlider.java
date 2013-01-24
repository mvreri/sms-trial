package com.example.android.customviews;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import dtd.phs.lib.utils.Logger;

public class TestSlider extends Activity {
	private SlidingDrawer slider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.test_slider);
		slider = (SlidingDrawer) findViewById(R.id.slider);
		slider.setOnDrawerOpenListener(new OnDrawerOpenListener() {
			@Override
			public void onDrawerOpened() {
				Logger.logInfo("Drawer opened");
			}
		});
		slider.setOnDrawerCloseListener(new OnDrawerCloseListener() {
			@Override
			public void onDrawerClosed() {
				Logger.logInfo("Drawer closed");
			}
		});
	}
}
