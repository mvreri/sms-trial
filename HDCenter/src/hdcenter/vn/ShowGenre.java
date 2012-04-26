package hdcenter.vn;

import hdcenter.vn.data.requests.ReqMoviesInGenre;
import hdcenter.vn.ui.MoviesListControl;
import hdcenter.vn.ui.Topbar;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.ListView;

public class ShowGenre extends Activity {

	protected static final String EXTRA_GENRE = "extra_genre";
	private static final int FIRST_PAGE = 1;
	private MoviesListControl moviesList;
	private String genre;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.sub_activity);
	    getInputData();
	    bindViews();
	}
	
	private void getInputData() {
		genre = getIntent().getStringExtra(EXTRA_GENRE);
	}

	private void bindViews() {
		bindTopbar();
		bindMoviesList();
	}

	private void bindMoviesList() {
		moviesList = new MoviesListControl(this, (ListView) findViewById(R.id.lvMovies), new Handler());
		moviesList.setRequest(new ReqMoviesInGenre(genre,FIRST_PAGE));
		moviesList.requestMovies();
	}

	private void bindTopbar() {
		new Topbar(this, findViewById(R.id.topbar));
	}

}
