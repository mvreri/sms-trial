package com.vsm.radio18.ui;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vsm.radio18.R;
import com.vsm.radio18.categories.RadioPagesAdapter;

public class TopTabs {

	private static final int[] INDICATOR_ID = { 
		R.id.layoutTabIndicator01,R.id.layoutTabIndicator02, R.id.layoutTabIndicator03 };

	private static final int[] TITLEs_ID = {
		R.id.tvChuyenMuc,
		R.id.tvHoiDap,
		R.id.tvHuongDan,
	};
	
	private ViewGroup root;
	private ViewGroup indicators;
	private TextView[] tvTitles;
	private View[] ind;

	//TODO: click on  tab -> change page
	public TopTabs(ViewGroup rootTabs, ViewGroup indicators) {
		this.root = rootTabs;
		this.indicators = indicators;
	}

	public void onCreate() {
		tvTitles = new TextView[RadioPagesAdapter.NUM_PAGEs];
		ind = new View[RadioPagesAdapter.NUM_PAGEs];
		for (int i = 0; i < RadioPagesAdapter.NUM_PAGEs; i++) {
			ind[i] = indicators.findViewById(INDICATOR_ID[i]);
			tvTitles[i] = (TextView) root.findViewById(TITLEs_ID[i]);
		}
	}

	public void selectTab(int i) {
		for(int j = 0 ; j < RadioPagesAdapter.NUM_PAGEs ; j++)
			if ( j != i ) {
				ind[j].setVisibility(View.INVISIBLE);
			} else {
				ind[j].setVisibility(View.VISIBLE);
			}
	}

	public void setIndicatorBackground(int bgColor) {
		indicators.setBackgroundColor(bgColor);
	}

}
