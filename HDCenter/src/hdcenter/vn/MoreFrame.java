package hdcenter.vn;

import hdcenter.vn.ui.SimpleTextAdapter;
import hdcenter.vn.utils.Helpers;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import dtd.phs.lib.ui.frames_host.FrameView;
/**
 * 
 * @author Pham Hung Son
 *	This tab contains:
 *	- Newly updated films: class NewMovies
 *	- Browse by genres: class MovieGenres
 *	- Browse collections (e.g: Top 250 IMDB , 007 collection ...): class MovieCollections
 * 	So it's a list - when user click on an item, the corresponding activity is lauched
 */
public class MoreFrame extends FrameView {


	static final int[] ITEM_NAMES_ID = { R.string.newly_updated, R.string.genres , R.string.collections };
	static final Class<?>[] ACTIVITIES = { NewMovies.class, MovieGenres.class, MovieCollections.class };
	private static final int ITEM_LAYOUT = R.layout.more_item_layout;
	private ListView listView;
	private SimpleTextAdapter adapter;
	public MoreFrame(Activity activity) {
		super(activity);
	}

	@Override
	public void onCreate(Context context) {
		Helpers.inflate(getContext(), R.layout.more, this);
		createViews();
	}

	private void createViews() {
		listView = (ListView) findViewById(R.id.lvMovies);
		adapter = new SimpleTextAdapter(getContext(),ITEM_LAYOUT,ITEM_NAMES_ID);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				Helpers.enterActivity(getHostedActivity(),new Intent(getHostedActivity(),ACTIVITIES[position]));
			}			
		});
	}
	


	@Override
	public void onResume() {
	}

	@Override
	public void onPause() {
	}

	@Override
	public Dialog onCreateDialog(int id) {
		return null;
	}

	@Override
	public void onPrepareDialog(int id) {
	}

	@Override
	public void onRefresh() {
	}
	
}
/*
	extends FrameView 
	implements IRequestListener {

	private ListView lvMovies;
	private Handler handler = new Handler();
	private MoviesList moviesList;
	private MovieAdapter adapter;

	public MoreFrame(Activity activity) {
		super(activity);
	}

	@Override
	public void onCreate(Context context) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.more, this);
		createViews();
	}
	//TODO: it doesn't work in the case there is "Load more..." button
	@Override
	public void onResume() {
		DataCenter.requestNewMovies(1, this, handler);
	}

	private void createViews() {
		lvMovies = (ListView) findViewById(R.id.lvMovies);
		lvMovies.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				MovieItem item = moviesList.get(position);
				ShowMovieDetails.passedSummaryItem = item;
				Intent i = new Intent(getContext(),ShowMovieDetails.class);
				hostedActivity.startActivity(i);
			}
		});		
	}

	@Override
	public Dialog onCreateDialog(int id) {
		return null;
	}

	@Override
	public void onPause() {

	}

	@Override
	public void onPrepareDialog(int id) {

	}

	@Override
	public void onRefresh() {

	}

	@Override
	public void onRequestError(Exception e) {
		Logger.logError(e);
	}


	@Override
	public void onRequestSuccess(Object data) {
		moviesList = (MoviesList) data;
		adapter = new MovieAdapter(getContext(), moviesList, handler);
		lvMovies.setAdapter(adapter);
	}




}
*/