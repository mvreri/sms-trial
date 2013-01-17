package com.example.android.customviews;

import phs.views.vol_button.NewKnobController;
import android.app.Activity;
import android.os.Bundle;

import com.example.android.customviews.R;

public class TestNewKnob extends Activity {
	private NewKnobController knobController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_new_knob);
		knobController = (NewKnobController) findViewById(R.id.knob_contr);
	}
}
