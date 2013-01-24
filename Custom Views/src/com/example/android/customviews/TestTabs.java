package com.example.android.customviews;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TestTabs extends Activity {
	private Button robo;
	private Button phone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_tabs);
		robo = (Button) findViewById(R.id.tabRobo);
		phone = (Button) findViewById(R.id.tabPhone);
		
		robo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				phone.setBackgroundResource(R.drawable.tab_bg_nor);
				robo.setBackgroundResource(R.drawable.tab_bg_pressed);
			}
		});
		phone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				robo.setBackgroundResource(R.drawable.tab_bg_nor);
				phone.setBackgroundResource(R.drawable.tab_bg_pressed);
			}
		});
	}
}
