package hdcenter.vn;

import hdcenter.vn.data.DataCenter;
import hdcenter.vn.data.IRequestListener;
import hdcenter.vn.entities.MovieItem;
import hdcenter.vn.entities.MoviesList;
import hdcenter.vn.ui.MovieAdapter;
import hdcenter.vn.utils.Logger;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import dtd.phs.lib.ui.frames_host.FrameView;
/**
 * 
 * @author Pham Hung Son
 *	This tab contains:
 *	- Newly updated films
 *	- Browse by genres
 *	- Browse collections (e.g: Top 250 IMDB , 007 collection ...)
 */
public class MoreFrame 
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
