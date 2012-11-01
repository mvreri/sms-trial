package dtd.phs.ultimate_browser;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * 
 * @author Pham Hung Son
 * Prototype:
 * Step 1: File browser
 * Step 2: 2 devices can browse each other content
 *
 */
public class LocalFileBrowser extends Activity {

	private ListView listview;
	private ArrayAdapter<String> adapter;
	private ArrayList<String> fileNames;
	private String currDir;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		listview = new ListView(getApplicationContext());
		setContentView(listview);
		fileNames = new ArrayList<String>();
		adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, fileNames);
		listview.setAdapter(adapter);
		setCurrentDir(Environment.getExternalStorageDirectory().toString());
	}

	private void setCurrentDir(String dir) {
		this.currDir = dir;
		File file = new File(dir);
		
	}
}
