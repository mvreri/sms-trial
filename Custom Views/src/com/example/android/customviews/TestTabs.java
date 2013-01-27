package com.example.android.customviews;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class TestTabs extends Activity {
	private static final int TAB_ROBO = 0;
	private static final int TAB_PHONE = 1;
	private Button robo;
	private Button phone;
	private int currentTab;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.test_tabs);
		robo = (Button) findViewById(R.id.tabRobo);
		phone = (Button) findViewById(R.id.tabPhone);
		
		robo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				selectRobo();
			}

		});
		phone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				selectPhone();
			}
		});
		
		currentTab = TAB_ROBO;
	}

	@Override
	protected void onResume() {
		super.onResume();
		if ( currentTab == TAB_ROBO) {
			selectRobo();
		} else {
			selectPhone();
		}
	}
	
	protected void selectRobo() {
		currentTab = TAB_ROBO;
		phone.setBackgroundResource(R.drawable.tab_bg_nor);
		phone.setTextColor(getResources().getColor(R.color.font_tab_title_nor));
		robo.setBackgroundResource(R.drawable.tab_bg_pressed);
		robo.setTextColor(getResources().getColor(R.color.font_tab_title_press));
	}

	protected void selectPhone() {
		currentTab = TAB_PHONE;
		robo.setBackgroundResource(R.drawable.tab_bg_nor);
		robo.setTextColor(getResources().getColor(R.color.font_tab_title_nor));
		phone.setBackgroundResource(R.drawable.tab_bg_pressed);
		phone.setTextColor(getResources().getColor(R.color.font_tab_title_press));
	}
}
