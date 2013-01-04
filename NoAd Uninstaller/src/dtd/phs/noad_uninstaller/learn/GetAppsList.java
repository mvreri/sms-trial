package dtd.phs.noad_uninstaller.learn;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import dtd.phs.lib.utils.Helpers;
import dtd.phs.lib.utils.Logger;
import dtd.phs.noad_uninstaller.R;

public class GetAppsList extends Activity {
	private ListView listview;
	private Button refresh;
	protected AppsAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.test_apps);
		listview = (ListView) findViewById(R.id.listview);
		refresh = (Button) findViewById(R.id.refresh);
		refresh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				long startTime = System.currentTimeMillis();
				ArrayList<PHS_AppInfo> appsInfo = Helpers.getAppsInfo(getApplicationContext());
				Logger.logInfo("Cout apps: " + appsInfo.size());
				adapter = new AppsAdapter(getApplicationContext());				
				listview.setAdapter(adapter);
				adapter.setData(appsInfo);
				long endTime = System.currentTimeMillis();
				long dur = endTime - startTime;
				Logger.logInfo("Loading time: " + dur);
			}
		});

		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				adapter.changeSelectedState(position);
			}
		});
	}
	


}
