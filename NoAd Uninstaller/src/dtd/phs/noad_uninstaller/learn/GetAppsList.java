package dtd.phs.noad_uninstaller.learn;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import dtd.phs.lib.utils.Logger;
import dtd.phs.noad_uninstaller.R;

public class GetAppsList extends Activity {
	private ListView listview;
	private Button refresh;
	protected AppsAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_apps);
		listview = (ListView) findViewById(R.id.listview);
		refresh = (Button) findViewById(R.id.refresh);
		refresh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				long startTime = System.currentTimeMillis();
				List<PHS_AppInfo> appsInfo = getAppsInfo(getApplicationContext());
				Logger.logInfo("Cout apps: " + appsInfo.size());
				adapter = new AppsAdapter(getApplicationContext(),appsInfo);
				listview.setAdapter(adapter);
				long endTime = System.currentTimeMillis();
				long dur = endTime - startTime;
				Logger.logInfo("Loading time: " + dur);
			}
		});

		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				PHS_AppInfo app = adapter.getItem(position);
				uninstall(app);
			}
		});
	}
	
	protected void uninstall(PHS_AppInfo app) {
		String packageName = app.getPackageName();
		Uri packageURI = Uri.parse("package:"+packageName);
		Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
		startActivity(uninstallIntent);
	}

	private List<PHS_AppInfo> getAppsInfo(Context context) {
		PackageManager packageManager = context.getPackageManager();
		List<ApplicationInfo> installedApplications = 
		   packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
		ArrayList<PHS_AppInfo> apps = new ArrayList<PHS_AppInfo>();
		for (ApplicationInfo appInfo : installedApplications)
		{
			int flags = appInfo.flags;
			if ( ( flags & ApplicationInfo.FLAG_SYSTEM ) == 0)
				apps.add(new PHS_AppInfo(appInfo.packageName, appInfo.loadLabel(packageManager).toString(), appInfo.loadIcon(packageManager)));
		} 

		return apps;

	}
}
