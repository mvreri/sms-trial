package com.vsm.radio18.test;

import java.util.ArrayList;

import dtd.phs.lib.utils.Helpers;
import dtd.phs.lib.utils.Logger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TestChooser extends Activity {
	static final String[] CLASSes = {
		"com.vsm.radio18.test.Test_DBOperations",
		"com.vsm.radio18.categories.ActMain"};
	private ArrayAdapter<String> adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ListView listview = new ListView(this);
		adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,getClassNames(CLASSes));
		listview.setAdapter(adapter);
		OnItemClickListener itemClick = new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
				String className = CLASSes[position];
				Class<?> act = null;
				try {
					act = Class.forName(className);
					startActivity(new Intent(getApplicationContext(), act));
				} catch (ClassNotFoundException e) {
					Logger.logError(e);
				}
				
			}
		};
		listview.setOnItemClickListener(itemClick);
		setContentView(listview);
	}
	private ArrayList<String> getClassNames(String[] classes2) {
		ArrayList<String> a = new ArrayList<String>();
		for(int i = 0 ; i < classes2.length; i++) {
			a.add(classes2[i].substring(classes2[i].lastIndexOf('.') + 1));
		}
		return a;
	}
}
