package com.vsm.radio18.test;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.vsm.radio18.data.ReqCategories;
import com.vsm.radio18.data.entities.CategoryItem;

import dtd.phs.lib.utils.Logger;

public class Test_ActStart extends Activity {

	public class CatAdapter extends BaseAdapter {

		
		private ArrayList<CategoryItem> categories;

		public CatAdapter() {
			categories = new ArrayList<CategoryItem>();
		}
		@Override
		public int getCount() {
			return categories.size();
		}

		@Override
		public CategoryItem getItem(int position) {
			return categories.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView v = new TextView(getApplicationContext());
			v.setText("" + categories.get(position).getId() + " ### " + categories.get(position).getName());
			return v;
		}

		public void refreshData(ArrayList<CategoryItem> categories) {
			this.categories = categories;
			notifyDataSetChanged();
		}

	}

	private CatAdapter adapter;
	private ListView listview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		listview = new ListView(this);
		adapter = new CatAdapter();
		listview.setAdapter(adapter);
		setContentView(listview);
		new Thread(new Runnable() {
			@Override
			public void run() {
				ReqCategories req = new ReqCategories();
				try {
					final ArrayList<CategoryItem> categories = (ArrayList<CategoryItem>) req.requestData();
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							adapter.refreshData(categories);
							listview.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> parent,
										View view, int position, long id) {
									CategoryItem cat = adapter.getItem(position);
									Intent i = new Intent(getApplicationContext(), Test_ActListArticles.class);
									i.putExtra(Test_ActListArticles.EXTRA_CAT_ID, cat.getId());
									startActivity(i);
								}
							});
						}
					});
					
				} catch (Exception e) {
					Logger.logError(e);
				}
			}
		}).start();
		
	}

}
