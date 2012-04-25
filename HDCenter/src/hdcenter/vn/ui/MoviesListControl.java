package hdcenter.vn.ui;

import hdcenter.vn.ShowMovieDetails;
import hdcenter.vn.data.DataCenter;
import hdcenter.vn.data.IRequestListener;
import hdcenter.vn.data.requests.Request;
import hdcenter.vn.entities.MovieItem;
import hdcenter.vn.entities.MoviesList;
import hdcenter.vn.utils.Helpers;
import hdcenter.vn.utils.Logger;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MoviesListControl 
	implements IRequestListener
{

	private Activity hostedActivity;
	private ListView listview;
	protected MoviesList moviesList = null;
	private MovieAdapter adapter = null;
	private Handler handler = null;
	private Request request;

	public MoviesListControl(Activity hostedActivity, ListView listview, Handler handler) {
		this.hostedActivity = hostedActivity;
		this.handler = handler;
		this.listview = listview;
		this.listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Helpers.verify( moviesList != null, "Movie list is NULL");
				Helpers.verify( moviesList.size() > position, "Position: " + position + " -- mvlist size: " + moviesList.size() );
				MovieItem item = moviesList.get(position);
				ShowMovieDetails.passedSummaryItem = item;
				Intent i = new Intent(
						MoviesListControl.this.hostedActivity.getApplicationContext(),
						ShowMovieDetails.class);
				MoviesListControl.this.hostedActivity.startActivity(i);
				
			}
		});
		 
	}

	@Override
	public void onRequestSuccess(Object data) {
		moviesList = (MoviesList) data;
		adapter = new MovieAdapter(hostedActivity.getApplicationContext(), moviesList, handler);
		listview.setAdapter(adapter);
	}

	@Override
	public void onRequestError(Exception e) {
		Logger.logError(e);
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public void requestMovies() {
		DataCenter.addRequest(request, this, handler);
	}

}
