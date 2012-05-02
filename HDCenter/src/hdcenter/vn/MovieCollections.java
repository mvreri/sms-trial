package hdcenter.vn;

import hdcenter.vn.data.requests.ReqCollections;
import hdcenter.vn.entities.MovieCollectionsList;
import hdcenter.vn.ui.CollectionsListControl;
import hdcenter.vn.ui.Topbar;
import hdcenter.vn.utils.Helpers;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.ListView;

public class MovieCollections 
	extends Activity 
//	implements IRequestListener 
{

	private static final int FIRST_PAGE = 1;
	private ListView lvCollections;
//	private MovieCollectionsList collections;
	private CollectionsListControl listControl;
	private Handler handler;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    handler = new Handler();
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
		listControl = new CollectionsListControl(this, lvCollections, null, handler);
		listControl.setRequest(new ReqCollections(FIRST_PAGE));
		listControl.requestFirstPage();
//		lvCollections.setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
//				String id = collections.getId(position);
//				Intent i = new Intent(getApplicationContext(),ShowCollection.class);
//				i.putExtra(ShowCollection.EXTRA_ID, id);
//				i.putExtra(ShowCollection.EXTRA_NAME, collections.getName(position));
//				Helpers.enterActivity(MovieCollections.this, i);
//			}
//		});
//		DataCenter.requestCollections(this,new Handler());
	}

	private void bindTopbar() {
		new Topbar(this,findViewById(R.id.topbar));
	}

//	@Override
//	public void onRequestSuccess(Object data) {
//		collections = (MovieCollectionsList) data;
//		BaseAdapter adapter = new CollectionsAdapter(getApplicationContext(),collections, new Handler());
//		lvCollections.setAdapter(adapter);
//	}
//
//	@Override
//	public void onRequestError(Exception e) {
//		Logger.logError(e);
//	}

}
