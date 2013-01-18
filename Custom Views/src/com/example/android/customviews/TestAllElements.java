package com.example.android.customviews;

import phs.views.PetrButton;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import dtd.phs.lib.utils.Helpers;

public class TestAllElements extends Activity {
	private PetrButton btnTransform;
	private PetrButton btnMute;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.test_all);
		btnTransform = (PetrButton) findViewById(R.id.btYes);
		btnTransform.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Helpers.showToast(TestAllElements.this, "Transform");
			}
		});
		
		btnMute = (PetrButton) findViewById(R.id.btNo);
		btnMute.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Helpers.showToast(TestAllElements.this, "Mute");
			}
		});
		
	}
}
