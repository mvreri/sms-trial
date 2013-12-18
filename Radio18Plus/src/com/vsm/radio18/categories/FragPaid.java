package com.vsm.radio18.categories;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.vsm.radio18.ActDetails;
import com.vsm.radio18.ActivityWithBottomBar;
import com.vsm.radio18.DBArticlesAdapter;
import com.vsm.radio18.R;
import com.vsm.radio18.data.ReqBalance;
import com.vsm.radio18.data.ReqCreateUser;
import com.vsm.radio18.data.db.DBCenter;
import com.vsm.radio18.data.db.QueryWorker;
import com.vsm.radio18.data.entities.DB_ArticelItem;

import dtd.phs.lib.data_framework.IDataListener;
import dtd.phs.lib.data_framework.IRequest;
import dtd.phs.lib.data_framework.RequestWorker;
import dtd.phs.lib.utils.Helpers;
import dtd.phs.lib.utils.Logger;
import dtd.phs.lib.utils.PreferenceHelpers;

public class FragPaid extends BaseFragment {

	private static final int FRAME_LOADING = 0;
	private static final int FRAME_DATA = 1;
	protected static final int FRAME_RETRY = 2;
	private ActivityWithBottomBar hostedAct;

	public static Fragment getInstance(ActivityWithBottomBar hostedAct) {
		FragPaid frag = new FragPaid();
		frag.hostedAct = hostedAct;
		return frag;
	}

	private FrameLayout mainFrames;
	private Handler handler = new Handler();
	private TextView tvBalance;
	private Button btRetry;
	private ListView listview;
	protected DBArticlesAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.page_paid, container, false);
		mainFrames = (FrameLayout) findViewById(R.id.main_frames);
		initBalanceModules();
		initDataFrame();
		initRetryFrame();
		return rootView;
	}

	private void initDataFrame() {
		listview = (ListView) findViewById(R.id.listview);
		adapter = new DBArticlesAdapter(getActivity());
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(itemClick);
	}
	
	OnItemClickListener itemClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
			DB_ArticelItem item = adapter.getItem(position);
			hostedAct.startPlayingMusic(item);
			handler.postDelayed(startDetailsActivity, 300);
		}
		
		Runnable startDetailsActivity = new Runnable() {
			@Override
			public void run() {
				Intent intent = new Intent(hostedAct,ActDetails.class);
				startActivity(intent);
			}
		};
	};


	private void initRetryFrame() {
		btRetry = (Button) findViewById(R.id.btRetry);
		btRetry.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				requestData();
			}
		});
	}

	private void initBalanceModules() {
		tvBalance = (TextView) findViewById(R.id.tvBalance);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		adapter.resume();
		requestData();
	}

	private void requestData() {
		Helpers.showOnlyView(mainFrames, FRAME_LOADING);
		requestPaidArticles();
		requestBalance();
	}

	private void requestPaidArticles() {
		QueryWorker.add(new Runnable() {
			@Override
			public void run() {
				ArrayList<DB_ArticelItem> allArticles = DBCenter.getAllArticles(getActivity());
				adapter.refreshData(allArticles);
			}
		});
	}

	private void requestBalance() {
		String userId = PreferenceHelpers.getUserId(getActivity());
		if (userId == null) {
			IRequest request = new ReqCreateUser(
					Helpers.getMacAddress(getActivity()));
			IDataListener listener = new IDataListener() {

				@Override
				public void onError(Exception e) {
					onRequestError(e);
				}

				@Override
				public void onCompleted(Object data) {
					if (data != null) {
						String userId = (String) data;
						PreferenceHelpers.setUserId(getActivity(), userId);
						requestBalance();
					} else
						onError(new Exception("Null data returned"));
				}
			};
			RequestWorker.addRequest(request, listener, handler);
		} else {
			IRequest request = new ReqBalance(userId);
			IDataListener listener = new IDataListener() {

				@Override
				public void onError(Exception e) {
					onRequestError(e);
				}

				@Override
				public void onCompleted(Object data) {
					if (data != null) {
						int amount = (Integer) data;
						tvBalance.setText("" + Helpers.formatRate(amount));
						Helpers.showOnlyView(mainFrames, FRAME_DATA);
					} else
						onError(new Exception("Null data returned"));
				}
			};
			RequestWorker.addRequest(request, listener, handler);
		}
	}

	protected void onRequestError(Exception e) {
		Logger.logError(e);
		Helpers.showOnlyView(mainFrames, FRAME_RETRY);
	}

	@Override
	public void onPause() {
		adapter.pause();
		super.onPause();
	}

}