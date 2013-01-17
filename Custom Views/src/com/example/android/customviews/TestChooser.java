package com.example.android.customviews;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class TestChooser extends Activity {

	final static String[] CLASSES = {
		"TestAudioButtons",
		"TestKnob",
		"TestNewKnob"
	};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Context context = this;
		final String packageName = this.getClass().getPackage().getName();

		ListView listview = new ListView(getApplicationContext());
		setContentView(listview);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, CLASSES);
		listview.setAdapter(adapter);

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
				try {
					Class c = Class.forName(packageName + "." + CLASSES[position]);
					startActivity(new Intent(context, c));
				} catch (ClassNotFoundException e) {
					Toast.makeText(context, String.valueOf(e), 5000).show();
				}

			}
		});

	}
}
