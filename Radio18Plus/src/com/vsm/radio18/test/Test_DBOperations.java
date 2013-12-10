package com.vsm.radio18.test;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.vsm.radio18.R;
import com.vsm.radio18.data.db.ArticlesTable;
import com.vsm.radio18.data.entities.DB_ArticelItem;
import com.vsm.radio18.data.entities.Item;

import dtd.phs.lib.utils.Logger;

public class Test_DBOperations extends Activity {
	private ListView listview;
	private Button btRequest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_db_operations);
		listview = (ListView) findViewById(R.id.listview);
		btRequest = (Button) findViewById(R.id.btRequest);
		btRequest.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						ArticlesTable tbl = null;
						try {
							tbl = new ArticlesTable(getApplicationContext());
							tbl.open();
							ArrayList<Item> all = tbl.getAll();
							//TODO: later - add to adapter data
						} catch (Exception e) {
							Logger.logError(e);
						} finally {
							try { tbl.close(); } catch (Exception e) {}
						}
					}
				}).start();
			}
		});
				
	}
	
 
}
