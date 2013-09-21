package com.vsm.radio18;

import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class DetailsBottomBar {
	public static final int UNINIT = 0;
	private View root;
	private SeekBar seekbar;
	private ImageView ivPlayPause;
	private TextView tvTitle;
	private TextView tvCurTime;
	private TextView tvDuration;
	private int progress;
	private int curTime;
	private int duration = UNINIT;

	public DetailsBottomBar(View root) {
		this.root = root;
	}
	
	private View findViewById(int id) {
		return root.findViewById(id);
	}

	public void onCreate() {
		seekbar = (SeekBar) findViewById(R.id.seekbar);
		ivPlayPause = (ImageView) findViewById(R.id.ivPlayPause);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvCurTime = (TextView) findViewById(R.id.tvCurTime);
		tvDuration = (TextView) findViewById(R.id.tvDuration);
	}

	public void onResume() {
	}

	public void onPause() {
	}
	
	public void setSongName(String name) {
		tvTitle.setText(name);
		updatePlayButtonState();
	}
}
