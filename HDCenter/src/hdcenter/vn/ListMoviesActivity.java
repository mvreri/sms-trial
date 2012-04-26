package hdcenter.vn;

import hdcenter.vn.data.requests.Request;
import hdcenter.vn.ui.MoviesListControl;
import hdcenter.vn.ui.Topbar;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.ListView;

public abstract class ListMoviesActivity extends Activity {
	/**
	 * @return request to be called on activity created 
	 */
	abstract protected Request provideRequest();
	
	/**
	 * Parse the data passed by the source activity
	 */
	abstract protected void getInputData();
	
	protected MoviesListControl moviesList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.sub_activity);
	    getInputData();
	    bindViews();
	}

	private void bindViews() {
		bindTopbar();
		bindMoviesList();
		
	}

	private void bindMoviesList() {
		moviesList = new MoviesListControl(this, (ListView) findViewById(R.id.lvMovies), new Handler());
		moviesList.setRequest(provideRequest());
		moviesList.requestMovies();
	}

	private void bindTopbar() {
		new Topbar(this, findViewById(R.id.topbar));
	}
	
	
}
