package hdcenter.vn;

import hdcenter.vn.data.requests.ReqRecommendMovies;
import hdcenter.vn.ui.MoviesListControl;
import hdcenter.vn.utils.Helpers;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.widget.ListView;
import dtd.phs.lib.ui.frames_host.FrameView;

public class RecommendFrame 
	extends FrameView 
{

	private static final int FIRST_PAGE = 1;
	private MoviesListControl movieList;
	private Handler handler = null;

	public RecommendFrame(Activity activity) {
		super(activity);
	}

	@Override
	public void onCreate(Context context) {
		handler = new Handler();
		Helpers.inflate(getContext(), R.layout.movies_listview, this);
		createViews();
		
	}

	@Override
	public void onResume() {
		
	}
	
	@Override
	public void onPause() {
	}

	private void createViews() {
		String title = getResources().getString(R.string.recommend_movies);
		
		movieList = new MoviesListControl(getHostedActivity(), (ListView) findViewById(R.id.lvMovies), title, handler);
		movieList.setRequest(new ReqRecommendMovies(FIRST_PAGE));
		movieList.requestFirstPage();
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
