package com.vsm.radio18.ui;

import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.vsm.radio18.R;

public class Topbar {

	private TextView tvTitle;
	private View root;
	private ImageView ivIcon;

	public Topbar(View view) {
		this.root = view;
	}
	
	public void onCreate() {
		ivIcon = (ImageView) root.findViewById(R.id.ivIcon);
		tvTitle = (TextView) root.findViewById(R.id.tvTitle);
	}
	
	public void setTitle(int stringRes) {
		tvTitle.setText(stringRes);
	}

	public void setTitle(String title) {
		tvTitle.setText(title);
	}

	public void setTopIcon(int icBack) {
		ivIcon.setImageResource(icBack);
	}

	public void setTopIconClick(OnClickListener onClickListener) {
		ivIcon.setOnClickListener(onClickListener);
	}
	
	public void alignCenterTitle() {
		tvTitle.setGravity(Gravity.CENTER);
	}

}
