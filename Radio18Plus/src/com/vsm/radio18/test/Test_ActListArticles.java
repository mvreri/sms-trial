package com.vsm.radio18.test;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.vsm.radio18.data.ReqListArticles;
import com.vsm.radio18.data.entities.ArticleItem;

import dtd.phs.lib.utils.Logger;

public class Test_ActListArticles extends Activity {

	public class ArticlesAdapter extends BaseAdapter {

		
		private ArrayList<ArticleItem> articles;

		public ArticlesAdapter() {
			articles = new ArrayList<ArticleItem>();
		}
		@Override
		public int getCount() {
			return articles.size();
		}

		@Override
		public ArticleItem getItem(int position) {
			return articles.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView v = new TextView(getApplicationContext());
			v.setText("" + articles.get(position).toString());
			return v;
		}

		public void refreshData(ArrayList<ArticleItem> list) {
			this.articles = list;
			notifyDataSetChanged();
		}

	}

	static final String EXTRA_CAT_ID = "extra_cat_id";

	private ArticlesAdapter adapter;
	protected long catId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ListView l = new ListView(this);
		adapter = new ArticlesAdapter();
		l.setAdapter(adapter);
		setContentView(l);
		Intent intent = getIntent();
		catId = intent.getLongExtra(EXTRA_CAT_ID, -1);
		new Thread(new Runnable() {
			@Override
			public void run() {
				ReqListArticles req = new ReqListArticles(catId);
				try {
					final ArrayList<ArticleItem> list = (ArrayList<ArticleItem>) req.requestData();
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							adapter.refreshData(list);
						}
					});
					
				} catch (Exception e) {
					Logger.logError(e);
				}
			}
		}).start();
		
	}

}

