package hdcenter.vn;

import hdcenter.vn.data.requests.ReqNewMovies;
import hdcenter.vn.ui.MoviesListView;
import hdcenter.vn.ui.Topbar;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.ListView;

public class NewMovies extends Activity {

	private static final int FIRST_PAGE = 1;
	private MoviesListView list;
	private Topbar topbar;
	private Handler handler = new Handler();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.sub_activity);
	    bindViews();
	}

	private void bindViews() {
		bindTopbar();
		bindListView();
	}

	private void bindListView() {
		list = new MoviesListView(this,(ListView)findViewById(R.id.lvMovies), handler);
		list.setRequest(new ReqNewMovies(FIRST_PAGE));
		list.requestMovies();
	}

	private void bindTopbar() {
		topbar = new Topbar(this, list ,findViewById(R.id.topbar));
	}

}
