package com.vsm.radio18.categories;

import com.vsm.radio18.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class FragPaid extends BaseFragment {

	public static Fragment getInstance() {
		return new FragPaid();
	}
	private FrameLayout mainFrames;
	private FrameLayout balanceFrames;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.page_paid, container, false);
		mainFrames = (FrameLayout) findViewById(R.id.main_frames);
		//TODO: load the balance seperatedly
		initBalanceModules();
//		initDataFrame();
//		initRetryFrame();
		return rootView;
	}

	private void initBalanceModules() {
		balanceFrames = (FrameLayout) findViewById(R.id.balance_frames);
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		requestData();
	}
	
	private void requestData() {
		requestBalance();
		requestPaidArticles();
	}

	@Override
	public void onPause() {
		super.onPause();
		
	};
}
