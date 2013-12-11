package com.vsm.radio18.test;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.vsm.radio18.DBArticlesAdapter;
import com.vsm.radio18.R;
import com.vsm.radio18.data.db.ArticlesTable;
import com.vsm.radio18.data.db.DBCenter;
import com.vsm.radio18.data.db.QueryWorker;
import com.vsm.radio18.data.entities.DB_ArticelItem;
import com.vsm.radio18.data.entities.Item;

import dtd.phs.lib.utils.Logger;

public class Test_DBOperations extends Activity {
	private ListView listview;
	private Button btRequest;
	private DBArticlesAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_db_operations);
		listview = (ListView) findViewById(R.id.listview);
		adapter = new DBArticlesAdapter(this);
		listview.setAdapter(adapter);
		
		btRequest = (Button) findViewById(R.id.btRequest);
		btRequest.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				QueryWorker.add(new Runnable() {
					@Override
					public void run() {
						ArrayList<DB_ArticelItem> a = DBCenter.getAllArticles(getApplicationContext());
						adapter.refreshData(a);
					}
				});
			}
		});
		
		
		OnItemClickListener itemListener = new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, final int position,long arg3) {
				QueryWorker.add(new Runnable() {
					@Override
					public void run() {
						DB_ArticelItem item = adapter.getItem(position);
						boolean remSucc = DBCenter.removeArticle(getApplicationContext(),item.getDBId());
						Logger.logInfo("Remove success ?  " + remSucc);
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								adapter.notifyDataSetChanged();	
							}
						});
					}
				});
			}
		};
		
		listview.setOnItemClickListener(itemListener);
				
	}
	
 
}
