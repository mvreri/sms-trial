package com.vsm.radio18.ui;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.vsm.radio18.R;

import dtd.phs.lib.utils.Helpers;

public class ListContentFrames {
	
	//FRAMES
	private static final int FRAME_LOADING = 0;
	private static final int FRAME_LISTVIEW = 1;
	private static final int FRAME_RETRY = 2;
	private FrameLayout mainFrames;	

	private View root;
	private View btRetry;
	private ListView listview;
	

	public ListContentFrames(View root) {
		this.root = root;
		bindViews();
	}

	private void bindViews() {
		mainFrames = (FrameLayout) findView(R.id.main_frames);
		listview = (ListView) findView(R.id.listview);
		btRetry = findView(R.id.btRetry);
	}

	public ListView getListview() {
		return listview;
	}
	
	public void setOnRetryClick(OnClickListener onClick) {
		btRetry.setOnClickListener(onClick);
	}
	
	public void showLoading() {
		showFrame(FRAME_LOADING);
	}
	
	public void showListview() {
		showFrame(FRAME_LISTVIEW);
	}
	
	public void showRetry() {
		showFrame(FRAME_RETRY);
	}

	private View findView(int id) {
		return root.findViewById(id);
	}	
	
	private void showFrame(int id) {
		Helpers.showOnlyView(mainFrames, id);
	}

}
