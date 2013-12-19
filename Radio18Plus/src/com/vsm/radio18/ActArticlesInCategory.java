package com.vsm.radio18;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.vsm.radio18.data.ReqListArticles;
import com.vsm.radio18.data.entities.ArticleItem;
import com.vsm.radio18.data.entities.CategoryItem;
import com.vsm.radio18.ui.Topbar;

import dtd.phs.lib.data_framework.IDataListener;
import dtd.phs.lib.data_framework.IRequest;
import dtd.phs.lib.data_framework.RequestWorker;
import dtd.phs.lib.utils.Helpers;
import dtd.phs.lib.utils.Logger;

public class ActArticlesInCategory extends ActivityWithBottomBar {

	public static final String EXTRA_CAT_NAME = "extra_cat_name";
	public static final String EXTRA_CAT_ID = "extra_cat_id";

	private static final int FRAME_LOADING = 0;
	private static final int FRAME_DATA = 1;
	private static final int FRAME_RETRY = 2;

	private Topbar topBar;
	private CategoryItem category;
	private ListView listview;
	private ArticlesAdapter adapter;
	private Handler handler = new Handler();

	private FrameLayout mainFrames;
	private Button btRetry;
	private IDataListener dataListener;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_articles_in_category);
		this.category = getCategory();
		if (category != null) {
			initTopbar();
			initListContent();
			initBottomControllers();
		} else {
			finish();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		adapter.onResume();
		requestData();
	}

	@Override
	protected void onPause() {
		adapter.onPause();
		super.onPause();
	}



	private CategoryItem getCategory() {
		String name = getIntent().getStringExtra(EXTRA_CAT_NAME);
		long id = getIntent().getLongExtra(EXTRA_CAT_ID, -1);
		if (id == -1) {
			Logger.logError("No cat id found ");
			return null;
		}
		return new CategoryItem(id, name);
	}

	private void initTopbar() {
		topBar = new Topbar(findViewById(R.id.top_bar));
		topBar.onCreate();
		topBar.setTopIcon(R.drawable.ic_back);
		topBar.setTopIconClick(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		topBar.setTitle(category.getName());
		topBar.alignCenterTitle();
	}

	private void initListContent() {
		mainFrames = (FrameLayout) findViewById(R.id.main_frames);
		listview = (ListView) findViewById(R.id.listview);
		btRetry = (Button) findViewById(R.id.btRetry);
		btRetry.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				requestData();
			}
		});
		adapter = new ArticlesAdapter(this);
		listview.setAdapter(adapter);

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ArticleItem item = adapter.getItem(position);
				if ( adapter.isPaid(position)) {
					startPlayingMusic(item);
					handler.postDelayed(startDetailsActivity, 300);
				} else adapter.processBuy(item);
			
			}
			
			Runnable startDetailsActivity = new Runnable() {
				@Override
				public void run() {
					Intent intent = new Intent(getApplicationContext(),ActDetails.class);
					startActivity(intent);
				}
			};

		});

	}


	private void requestData() {
		Helpers.showOnlyView(mainFrames, FRAME_LOADING);
		IRequest request = new ReqListArticles(category.getId());
		dataListener = new IDataListener() {

			@Override
			public void onError(Exception e) {
				Logger.logError(e);
				Helpers.showOnlyView(mainFrames, FRAME_RETRY);
			}

			@Override
			public void onCompleted(Object data) {
				if (data != null) {
					@SuppressWarnings("unchecked")
					ArrayList<ArticleItem> list = (ArrayList<ArticleItem>) data;
					adapter.refreshData(list);
					Helpers.showOnlyView(mainFrames, FRAME_DATA);
				} else {
					onError(new RuntimeException("null data returned"));
				}
			}
		};
		RequestWorker.addRequest(request, dataListener, handler);
	}



}
