package com.example.android.customviews;

import phs.views.LomoTabs;

import com.example.android.customviews.TestTabButtons.Holder;
import com.example.android.customviews.TestTabButtons.MyAdapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import dtd.phs.lib.utils.Helpers;
import dtd.phs.lib.utils.Logger;

public class TestSlider extends Activity {
	private LomoTabs tabs;
	private ListView listview;
	private MyAdapter adapter;
	private int currentTab = TAB_ROBO;
	static private final String[] NAMES_ROBO = {"GANG7.MRB","GANG7.MP3","BILL.MRB","BILL.MP3","YESTERDAY.MRB","YESTERDAY.MP3"};
	static private final String[] SIZE_ROBO = {"140kB","3.4MB","120kB","4.2MB","80kB","5.1MB"};

	static private final String[] NAMES_PHONE = {"CHUOI.MRB","CHUOI.MP3","SEN.MRB","SEN.MP3","DAUPHU.MRB","DAUPHU.MP3","CHUOI.MRB","CHUOI.MP3","SEN.MRB","SEN.MP3","DAUPHU.MRB","DAUPHU.MP3","CHUOI.MRB","CHUOI.MP3","SEN.MRB","SEN.MP3","DAUPHU.MRB","DAUPHU.MP3"};
	static private final String[] SIZE_PHONE = {"140kB","3.4MB","120kB","4.2MB","80kB","5.1MB","140kB","3.4MB","120kB","4.2MB","80kB","5.1MB","140kB","3.4MB","120kB","4.2MB","80kB","5.1MB"};

	private static final int TAB_ROBO = 0;
	private static final int TAB_PHONE = 1;
	
	
	private SlidingDrawer slider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.test_slider);
		slider = (SlidingDrawer) findViewById(R.id.slider);
		slider.setOnDrawerOpenListener(new OnDrawerOpenListener() {
			@Override
			public void onDrawerOpened() {
				Logger.logInfo("Drawer opened");
			}
		});
		slider.setOnDrawerCloseListener(new OnDrawerCloseListener() {
			@Override
			public void onDrawerClosed() {
				Logger.logInfo("Drawer closed");
			}
		});
		

		tabs = (LomoTabs) findViewById(R.id.tabs);
		tabs.setOnClickLeftTab(new OnClickListener() {
			@Override
			public void onClick(View v) {
				currentTab = TAB_ROBO;
				showRoboData();
			}
		});
		
		tabs.setOnClickRightTab(new OnClickListener() {
			@Override
			public void onClick(View v) {
				currentTab = TAB_PHONE;
				showPhoneData();
			}
		});
		
		listview = (ListView) findViewById(R.id.listview);
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		if ( currentTab == TAB_PHONE) {
			showPhoneData();
		} else showRoboData();
	}

	protected void showPhoneData() {
		adapter = new MyAdapter(NAMES_PHONE,SIZE_PHONE);
		listview.setAdapter(adapter);
	}

	protected void showRoboData() {
		adapter = new MyAdapter(NAMES_ROBO,SIZE_ROBO);
		listview.setAdapter(adapter);
	}

	public class Holder {
		public TextView tvName;
		public TextView tvSize;
	}
	
	public class MyAdapter extends BaseAdapter {
		private static final String NAME_FONT = "fonts/Roboto-Light.ttf";
		private String[] names;
		private String[] size;

		public MyAdapter(String[] names, String[] size) {
			this.names = names;
			this.size = size;
			
		}

		@Override
		public int getCount() {
			return size.length;
		}

		@Override
		public Object getItem(int position) {
			return names[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			Holder holder = null;
			if ( v == null ) {
				v = Helpers.inflate(getApplicationContext(), R.layout.list_item, null);
				holder = new Holder();
				holder.tvName = (TextView) v.findViewById(R.id.tvName);
				holder.tvSize = (TextView) v.findViewById(R.id.tvSize);
				
				v.setTag(holder);
			} else {
				holder = (Holder) v.getTag();
			}
			Typeface face=Typeface.createFromAsset(getAssets(), NAME_FONT);

			holder.tvName.setTypeface(face);
			holder.tvName.setText(names[position]);
			
			holder.tvSize.setTypeface(face);
			holder.tvSize.setText(size[position]);
			
			return v;
		}

	}	
	
}
