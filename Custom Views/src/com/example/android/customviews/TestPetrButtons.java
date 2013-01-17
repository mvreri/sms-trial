package com.example.android.customviews;

import phs.views.PetrButton;
import android.app.Activity;
import android.os.Bundle;

public class TestPetrButtons extends Activity {
	private PetrButton helloButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_petr_buttons);
		helloButton = (PetrButton) findViewById(R.id.btHello);
		
	}
}
