package hdcenter.vn;

import hdcenter.vn.data.requests.ReqCinemaMovies;
import hdcenter.vn.ui.CinemaMovieListControl;
import hdcenter.vn.ui.EndlessListControl;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.widget.ListView;
import dtd.phs.lib.ui.frames_host.FrameView;

public class CinemaFrame extends FrameView {

	private static final int FIRST_PAGE = 1;
	private EndlessListControl moviesList;

	public CinemaFrame(Activity activity) {
		super(activity);
	}

	@Override
	public void onCreate(Context context) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.movies_listview, this);
		createViews();
	}

	private void createViews() {
		String title = getResources().getString(R.string.cinema_movies);
		moviesList = new CinemaMovieListControl(getHostedActivity(), (ListView) findViewById(R.id.lvMovies), title, new Handler());
		moviesList.setRequest(new ReqCinemaMovies(FIRST_PAGE));
		moviesList.requestFirstPage();
	}

	@Override
	public Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPrepareDialog(int id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub

	}

}
