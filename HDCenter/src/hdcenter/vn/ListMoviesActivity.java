package hdcenter.vn;

import hdcenter.vn.data.requests.RequestMoviesList;
import hdcenter.vn.ui.MoviesListControl;
import hdcenter.vn.ui.Topbar;
import hdcenter.vn.utils.Helpers;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.ListView;
/**
 * 
 * @author hungson175 
 * <pre>
 * This class is the base class for all activities with the following format:
 * - Topbar (Go home - search edittext - Search button)
 * - List of movies
 * They are only different by 
 *  1. The data passed in by the source activity (to implement: getInputData() )
 *  2. Request to get the movies from the server (to implement: provideRequest() )
 * </pre>
 *
 */
public abstract class ListMoviesActivity extends Activity {
	/**
	 * @return request to be called on activity created 
	 */
	abstract protected RequestMoviesList provideRequest();
	
	/**
	 * Parse the data passed by the source activity
	 */
	abstract protected void getInputData();
	
	/**
	 * @return title for the movie list 
	 */
	abstract protected String getListTitle();
	
	protected MoviesListControl moviesList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.sub_activity);
	    getInputData();
	    bindViews();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Helpers.hideSoftKeyboard(this);
	}

	private void bindViews() {
		bindTopbar();
		bindMoviesList();
		
	}

	private void bindMoviesList() {
		moviesList = new MoviesListControl(this, (ListView) findViewById(R.id.lvMovies), getListTitle(), new Handler());
		moviesList.setRequest(provideRequest());
		moviesList.requestFirstPage();
	}

	private void bindTopbar() {
		new Topbar(this, findViewById(R.id.topbar));
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Helpers.exitTransition(this);
	}
	
	
}
