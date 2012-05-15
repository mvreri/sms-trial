package hdcenter.vn.ui;

import hdcenter.vn.ShowMovieDetails;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.widget.ListView;

public class CinemaMovieListControl extends MoviesListControl {

	public CinemaMovieListControl(Activity hostedActivity, ListView listview,String title, Handler handler) {
		super(hostedActivity, listview, title, handler);
	}
	
	@Override
	public void addExtra(Intent i) {	
		super.addExtra(i);
		i.putExtra(ShowMovieDetails.EXTRA_MODE, ShowMovieDetails.MODE_DETAILS);
	}

}
