package hdcenter.vn.ui;

import hdcenter.vn.ShowMovieDetails;
import hdcenter.vn.entities.MovieItem;
import hdcenter.vn.entities.MoviesList;
import hdcenter.vn.utils.Helpers;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.widget.BaseAdapter;
import android.widget.ListView;

public class MoviesListControl extends EndlessListControl {

	public MoviesListControl(Activity hostedActivity, ListView listview,String title, Handler handler) {
		super(hostedActivity, listview, title, handler);
	}

	@Override
	protected BaseAdapter createAdapter(
			Activity hostedActivity,
			ArrayList<?> itemsList, 
			Handler handler) {
		return new MovieAdapter(hostedActivity, (MoviesList) itemsList, handler);
	}

	@Override
	protected ArrayList<?> provideEmptyData() {
		return new MoviesList();
	}

	@Override
	protected void appendItems(ArrayList<?> itemsList,ArrayList<?> additionalData) {
		((MoviesList) itemsList).append((MoviesList)additionalData);
	}

	@Override
	public void onListItemClick(int position, Object item) {
		
		ShowMovieDetails.passedSummaryItem = (MovieItem) item;
		Activity hostedActivity = getHostedActivity();
		Intent i = new Intent(
				hostedActivity.getApplicationContext(),
				ShowMovieDetails.class);
		Helpers.enterActivity(hostedActivity, i);

	}


}
