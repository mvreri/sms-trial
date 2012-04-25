package hdcenter.vn;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class MovieGenres extends Activity {

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
		// TODO Auto-generated method stub
		
	}

	private void bindTopbar() {
		
	}

}
