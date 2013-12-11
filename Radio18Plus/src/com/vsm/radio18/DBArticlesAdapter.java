package com.vsm.radio18;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vsm.radio18.data.entities.DB_ArticelItem;

import dtd.phs.lib.utils.Helpers;

public class DBArticlesAdapter extends BaseAdapter 
{

	private Activity act;
	private volatile ArrayList<DB_ArticelItem> a = new ArrayList<DB_ArticelItem>();

	public DBArticlesAdapter(Activity act) {
		this.act = act;
	}
	@Override
	public int getCount() {
		return a.size();
	}

	@Override
	public DB_ArticelItem getItem(int position) {
		return a.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO optimized later
		View v = Helpers.inflate(act, R.layout.test_db_article_item, null);
		DB_ArticelItem item = getItem(position);
		((TextView)v.findViewById(R.id.tvName)).setText(item.getName());
		return v;
	}
	
	public void refreshData(ArrayList<DB_ArticelItem> a) {
		this.a.clear();
		for(int i = 0 ; i < a.size() ; i++) 
			this.a.add(a.get(i));
		act.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				notifyDataSetChanged();
			}
		});
	}
	
	public void removeItem(int position) {
		a.remove(position);
		act.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				notifyDataSetChanged();
			}
		});
	}

}
