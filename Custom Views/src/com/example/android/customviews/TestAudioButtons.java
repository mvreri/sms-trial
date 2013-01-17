package com.example.android.customviews;

import phs.views.audio_buttons.AudioButtons;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;


import dtd.phs.lib.utils.Helpers;

public class TestAudioButtons extends Activity {
	private AudioButtons audioButtons;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_audio_buttons);
		audioButtons = (AudioButtons) findViewById(R.id.audio_buttons);
		audioButtons.setOnPreviousListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Helpers.showToast(TestAudioButtons.this, "Previous");
			}
		});

		audioButtons.setOnNextListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Helpers.showToast(TestAudioButtons.this, "Next");
			}
		});

		audioButtons.setOnPlayListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Helpers.showToast(TestAudioButtons.this, "Play/Pause");
				//TODO: change the button 
			}
		});
		
		
		
	}
}
