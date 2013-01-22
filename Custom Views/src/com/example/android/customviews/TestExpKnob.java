package com.example.android.customviews;

import phs.views.vol_button.ExperimentKnobController;
import android.app.Activity;
import android.os.Bundle;

public class TestExpKnob extends Activity {
	private ExperimentKnobController knobController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_exp_knob);
		knobController = (ExperimentKnobController) findViewById(R.id.knob_contr);
	}
}
