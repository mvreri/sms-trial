package com.vsm.radio18.categories;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.ViewGroup;

import com.vsm.radio18.ActivityWitghBottomBar;
import com.vsm.radio18.R;
import com.vsm.radio18.ui.TopTabs;
import com.vsm.radio18.ui.Topbar;

import dtd.phs.lib.ui.images_loader.ImageCache;
import dtd.phs.lib.utils.PreferenceHelpers;

public class ActMain extends ActivityWitghBottomBar {
	private static final int DEFAULT_TAB = 0;
	private Topbar topbar;
	private TopTabs topTabs;
	private ViewPager pager;
	private Handler handler = new Handler();
	private RadioPagesAdapter pagesAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		clearCacheFirstTimeRunning();
		setContentView(R.layout.act_main);
		initTopbar();
		initTopTabs();
		initPages();
		initBottomControllers();
	}

	protected void clearCacheFirstTimeRunning() {
		if ( PreferenceHelpers.firstTimeRunning(getApplicationContext()) ) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					ImageCache.clearCachedDir();		
				}
			}).start();
			
		}
	}

	private void initTopbar() {
		topbar = new Topbar((ViewGroup) findViewById(R.id.top_bar));
		topbar.onCreate();
	}

	private void initTopTabs() {
		topTabs = new TopTabs(
				(ViewGroup) findViewById(R.id.top_tab),
				(ViewGroup) findViewById(R.id.tab_indicator));
		topTabs.onCreate();
		topTabs.selectTab(DEFAULT_TAB);
		// TODO: link toptabs & pager
	}
	

	private void initPages() {
		pager = (ViewPager) findViewById(R.id.pager);
		pagesAdapter = new RadioPagesAdapter(getSupportFragmentManager(), getApplicationContext(), handler);
		pager.setAdapter(pagesAdapter);
		pager.setCurrentItem(DEFAULT_TAB);

		//TODO: click on  tab -> change page
		pager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				topTabs.selectTab(position);
				if ( position == RadioPagesAdapter.FRAG_CATEGORIES ) 
					topTabs.setIndicatorBackground(getResources().getColor(R.color.main_bg));
				else topTabs.setIndicatorBackground(getResources().getColor(R.color.main_bg));
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {}
			@Override
			public void onPageScrollStateChanged(int arg0) {}
		});
	}

}
