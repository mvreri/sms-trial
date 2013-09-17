package com.vsm.radio18.categories;

import dtd.phs.lib.utils.Helpers;
import android.content.Context;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class RadioPagesAdapter extends FragmentPagerAdapter {

	public static final int NUM_PAGEs = 3;
	public static final int FRAG_CATEGORIES = 0;
	public static final int FRAG_QNA = 1;
	public static final int FRAG_HELP = 2;
	private Context context;
	private Handler handler;

	public RadioPagesAdapter(FragmentManager fMan, Context context,
			Handler handler) {
		super(fMan);
		this.context = context;
		this.handler = handler;
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
		case FRAG_CATEGORIES:
			return FragCategories.getInstance();
		case FRAG_QNA:
			return FragQNA.getInstance();
		case FRAG_HELP:
			return FragHelp.getInstance();
		default:
			Helpers.assertCondition(false);
			return null;
		}
	}

	@Override
	public int getCount() {
		return NUM_PAGEs;
	}

}
