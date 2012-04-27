package hdcenter.vn;

import hdcenter.vn.data.DataCenter;
import hdcenter.vn.data.IRequestListener;
import hdcenter.vn.entities.MovieCollectionsList;
import hdcenter.vn.ui.SimpleTextAdapter;
import hdcenter.vn.ui.Topbar;
import hdcenter.vn.utils.Helpers;
import hdcenter.vn.utils.Logger;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MovieCollections 
	extends Activity 
	implements IRequestListener 
{

	private ListView lvCollections;
	private MovieCollectionsList collections;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.sub_activity);
	    bindViews();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Helpers.hideSoftKeyboard(this);
	}

	private void bindViews() {
		bindTopbar();
		bindListview();
	}

	private void bindListview() {
		lvCollections = (ListView) findViewById(R.id.lvMovies);
		lvCollections.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				String id = collections.getId(position);
				Intent i = new Intent(getApplicationContext(),ShowCollection.class);
				i.putExtra(ShowCollection.EXTRA_ID, id);
				Helpers.enterActivity(MovieCollections.this, i);
			}
		});
		DataCenter.requestCollections(this,new Handler());
	}

	private void bindTopbar() {
		new Topbar(this,findViewById(R.id.topbar));
	}

	@Override
	public void onRequestSuccess(Object data) {
		collections = (MovieCollectionsList) data;
		SimpleTextAdapter adapter = new SimpleTextAdapter(getApplicationContext(), R.layout.more_item_layout, collections.getNames());
		lvCollections.setAdapter(adapter);
	}

	@Override
	public void onRequestError(Exception e) {
		Logger.logError(e);
	}

}
