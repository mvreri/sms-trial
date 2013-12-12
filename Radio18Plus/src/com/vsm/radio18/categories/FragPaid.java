package com.vsm.radio18.categories;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.vsm.radio18.R;
import com.vsm.radio18.data.ReqBalance;
import com.vsm.radio18.data.ReqCreateUser;

import dtd.phs.lib.data_framework.IDataListener;
import dtd.phs.lib.data_framework.IRequest;
import dtd.phs.lib.data_framework.RequestWorker;
import dtd.phs.lib.utils.Helpers;
import dtd.phs.lib.utils.Logger;
import dtd.phs.lib.utils.PreferenceHelpers;

public class FragPaid extends BaseFragment {

	private static final int FRAME_LOADING = 0;
	private static final int FRAME_DATA = 1;

	public static Fragment getInstance() {
		return new FragPaid();
	}

	private FrameLayout mainFrames;
	private Handler handler = new Handler();
	private TextView tvBalance;
	private boolean isLoadingArticles;
	private boolean isLoadingBalance;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.page_paid, container, false);
		mainFrames = (FrameLayout) findViewById(R.id.main_frames);
		// TODO: load the balance seperatedly
		initBalanceModules();
		// initDataFrame();
		// initRetryFrame();
		return rootView;
	}

	private void initBalanceModules() {
		tvBalance = (TextView) findViewById(R.id.tvBalance);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		requestData();
	}

	private void requestData() {
		Helpers.showOnlyView(mainFrames, FRAME_LOADING);
		isLoadingBalance = true;
		isLoadingArticles = true;
		startCheckThread(); 
		//TODO: automatically run, check every 0.5 seconds if 2 loading activities is done, (done -> show the data frame)
		
		requestBalance();
		// requestPaidArticles(); TODO
	}

	private void requestBalance() {
		if (PreferenceHelpers.isAccountCreated(getActivity())) {

			IDataListener listener = new IDataListener() {
				@Override
				public void onError(Exception e) {
					Logger.logError(e);
					tvBalance.setText("");
					isLoadingBalance = false;
				}

				@Override
				public void onCompleted(Object data) {
					if (data == null) {
						onError(new Exception("Null data returned"));
					} else {
						tvBalance.setText("" + ((Integer) data));
					}
					isLoadingBalance = false;
				}
			};
			IRequest request = new ReqBalance(Helpers.getUserCodeForThisPhone(getActivity()));
			RequestWorker.addRequest(request, listener, handler);
		} else {
			IRequest request = new ReqCreateUser(Helpers.getUserCodeForThisPhone(getActivity()));
			IDataListener listener = new IDataListener() {
				
				@Override
				public void onError(Exception e) {
					Logger.logError(e);
					tvBalance.setText("");
				}
				
				@Override
				public void onCompleted(Object data) {
					PreferenceHelpers.setAccountCreated(getActivity(),true);
					requestBalance();
				}
			};
			RequestWorker.addRequest(request, listener, handler);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		stopCheckThread(); //TODO: if needed
	};
}
