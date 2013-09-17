package com.vsm.radio18.categories;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.vsm.radio18.ActArticlesInCategory;
import com.vsm.radio18.R;
import com.vsm.radio18.data.ReqCategories;
import com.vsm.radio18.data.entities.CategoryItem;

import dtd.phs.lib.data_framework.IDataListener;
import dtd.phs.lib.data_framework.RequestWorker;
import dtd.phs.lib.utils.Helpers;
import dtd.phs.lib.utils.Logger;

public class FragCategories extends BaseFragment {

	private static final int FRAME_LOADING = 0;
	private static final int FRAME_DATA = 1;
	private static final int FRAME_RETRY = 2;
	
	private FrameLayout mainFrames;
	private ListView listview;
	private CategoriesAdapter catAdapter;
	private Handler handler = new Handler();
	private Button btRetry;
	private IDataListener dataListener;


	public FragCategories() {
		super();
	}
	
	public static FragCategories getInstance() {
		return new FragCategories();
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.list_content, container, false);
		mainFrames = (FrameLayout) findViewById(R.id.main_frames);

		initDataFrame();
		initRetryFrame();
		
		return rootView;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		requestData();
	}

	protected void initDataFrame() {
		listview = (ListView) findViewById(R.id.listview);
		catAdapter = new CategoriesAdapter(getActivity());
		listview.setAdapter(catAdapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				CategoryItem item = catAdapter.getItem(position);
				Intent i = new Intent(getActivity(), ActArticlesInCategory.class);
				i.putExtra(ActArticlesInCategory.EXTRA_CAT_ID, item.getId());
				i.putExtra(ActArticlesInCategory.EXTRA_CAT_NAME, item.getName());
				startActivity(i);
			}
		});	
	}

	protected void initRetryFrame() {
		btRetry = (Button) findViewById(R.id.btRetry);
		btRetry.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				requestData();
			}
		});
	}

	protected void requestData() {
		Helpers.showOnlyView(mainFrames, FRAME_LOADING);
		dataListener = new IDataListener() {
			
			@Override
			public void onError(Exception e) {
				Logger.logError(e);
				Helpers.showOnlyView(mainFrames, FRAME_RETRY);
			}
			
			@Override
			public void onCompleted(Object data) {
				if ( data != null ) {
					ArrayList<CategoryItem> list = (ArrayList<CategoryItem>) data;
					catAdapter.refreshData(list);
					Helpers.showOnlyView(mainFrames, FRAME_DATA);
				} else {
					onError(new RuntimeException("Null data returned"));
				}
			}
		};
		RequestWorker.addRequest(new ReqCategories(), dataListener, handler);
	}
	
}
