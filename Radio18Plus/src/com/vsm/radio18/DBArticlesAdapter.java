package com.vsm.radio18;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vsm.radio18.data.entities.DB_ArticelItem;

import dtd.phs.lib.ui.images_loader.ImageLoader;
import dtd.phs.lib.utils.Helpers;

public class DBArticlesAdapter extends BaseAdapter 
{



	private Activity act;
	private volatile ArrayList<DB_ArticelItem> a = new ArrayList<DB_ArticelItem>();
	private Handler handler = new Handler();
	private ImageLoader imageloader;

	public DBArticlesAdapter(Activity act) {
		this.act = act;
	}
	@Override
	public int getCount() {
		return a.size();
	}
	
	public void resume() {
		imageloader = new ImageLoader(act, handler, null);
	}
	
	public void pause() {
		imageloader.stop();
	}

	@Override
	public DB_ArticelItem getItem(int position) {
		return a.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public class Holder {
		ImageView ivCover;
		TextView tvName;
		TextView tvDesc;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		Holder holder = null;
		if ( v == null) {
			v = Helpers.inflate(act, R.layout.db_article_item, null);
			holder = new Holder();
			holder.ivCover = (ImageView) v.findViewById(R.id.ivCover);
			holder.tvName = (TextView) v.findViewById(R.id.tvName);
			holder.tvDesc = (TextView) v.findViewById(R.id.tvDesc);
			v.setTag(holder);
		} else {
			holder = (Holder) v.getTag();
		}
		DB_ArticelItem item = getItem(position);
		holder.tvDesc.setText(item.getDesc());
		holder.tvName.setText(item.getName());
		imageloader.loadImage(item.getCoverURL(), holder.ivCover);
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
